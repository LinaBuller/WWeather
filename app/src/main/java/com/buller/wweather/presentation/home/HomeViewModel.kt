package com.buller.wweather.presentation.home

import android.location.Location
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.buller.wweather.domain.location.LocationTracker
import com.buller.wweather.domain.repository.RoomRepository
import com.buller.wweather.domain.repository.WeatherRepository
import com.buller.wweather.domain.util.Result
import com.buller.wweather.domain.model.WeatherInfo
import com.buller.wweather.domain.model.City
import com.buller.wweather.domain.model.PreferencesState
import com.buller.wweather.domain.repository.UserPreferencesRepository
import com.buller.wweather.domain.repository.WorkManagerRepository
import com.buller.wweather.presentation.cities.CitiesState
import com.buller.wweather.presentation.search.LocationState
import com.buller.wweather.presentation.search.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val roomRepository: RoomRepository,
    private val locationTracker: LocationTracker,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val workerManagerRepository: WorkManagerRepository
) : ViewModel() {

    private var citiesViewModelState =
        MutableStateFlow(CitiesState(cities = emptyList(), isLoading = true))

    val citiesUiState = citiesViewModelState.map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, citiesViewModelState.value.toUiState())

    private var viewModelState = MutableStateFlow(PagerState(pagerList = emptyList()))

    val uiState = viewModelState.map(PagerState::toUiState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    init {
        refreshCities()
        refreshMainWeather()
        initLoadPref()
        viewModelScope.launch {
            roomRepository.citiesDeletedEvent.collect {
                refreshCities()
                refreshMainWeather()
            }
        }
        uploadWeather()
    }


    fun fetchWeather(lifecycleOwner: LifecycleOwner) {
        workerManagerRepository.fetchWeather(lifecycleOwner)
    }


    private fun uploadWeather() {
        viewModelScope.launch {
            repository.citiesUpdateEvent.collect { newData ->
                viewModelState.update { state ->
                    state.copy(pagerList = newData, isLoading = false)
                }
            }
        }
    }

    fun refreshMainWeather() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val location = locationTracker.getCurrentLocation()
            if (location != null) {
                val savedCities = citiesViewModelState.value.cities
                if (savedCities.isEmpty()) {
                    loadCityFromLocation(location)
                } else {
                    loadCitiesFromLocalDatabase(savedCities)
                }
            } else {
                viewModelState.update {
                    it.copy(
                        isLoading = false,
                        error = Exception("Location not found")
                    )
                }
            }
        }
    }

    private fun setCityInLocalDatabase(data: WeatherInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            if (data.currentWeatherData != null) {
                roomRepository.setCity(getCityFromWeatherInfo(data))
            }
        }
    }

    private fun getCityFromWeatherInfo(data: WeatherInfo): City {
        return City(
            name = data.currentWeatherData?.cityName ?: "",
            region = data.currentWeatherData?.region ?: "",
            country = data.currentWeatherData?.country ?: "",
            isPin = true,
            position = 0
        )
    }

    fun refreshCities() {
        citiesViewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = roomRepository.getCities()
            result.collect { res ->
                citiesViewModelState.update { currentState ->
                    when (res) {
                        is Result.Success -> {
                            val uniqueCities = res.data.distinctBy { it.name }
                            currentState.copy(
                                cities = uniqueCities,
                                isLoading = false
                            )
                        }

                        is Result.Error -> currentState.copy(
                            isLoading = false,
                            errorMessages = res.exception
                        )

                        is Result.Loading -> currentState.copy(isLoading = true)
                    }
                }
            }
        }
    }

    private suspend fun loadCityFromLocation(location: Location) {
        val locationStr = StringBuilder().append(location.latitude).append(",")
            .append(location.longitude)
        val result = repository.getWeatherData(
            q = locationStr.toString(),
            days = 3,
            aqi = "no",
            alerts = "no"
        )
        result.collect { res ->
            withContext(Dispatchers.IO) {
                viewModelState.update {
                    when (res) {
                        is Result.Success -> {
                            setCityInLocalDatabase(res.data)
                            val city = getCityFromWeatherInfo(res.data)
                            val page = ExamplePage(
                                city = city,
                                weatherInfo = res.data,
                                isLoading = false
                            )

                            it.copy(pagerList = listOf(page), isLoading = false)
                        }

                        is Result.Error -> {
                            it.copy(pagerList = emptyList(), error = res.exception)
                        }

                        is Result.Loading -> {
                            it.copy(pagerList = emptyList(), isLoading = true)
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun loadCitiesFromLocalDatabase(cities: List<City>) {
        val results = mutableMapOf<String, ExamplePage>()
        val flowOfCities = cities.map { city ->
            flow {
                emit(city to Result.Loading)
                repository.getWeatherData(q = city.name, days = 3, aqi = "no", alerts = "no")
                    .collect { result ->
                        emit(city to result)
                    }
            }
        }
        flowOfCities.asFlow().flattenMerge(concurrency = 5).collect { (city, result) ->
            when (result) {
                is Result.Success -> {
                    val existingCity = roomRepository.getCityByName(city.name)
                    if (existingCity == null) {
                        setCityInLocalDatabase(result.data)
                    }

                    city.currentTempC = result.data.currentWeatherData?.temperatureC.toString()
                    city.condition = result.data.currentWeatherData?.weatherType
                    city.currentTimestamp = result.data.currentWeatherData?.timestamp ?: 123412
                    city.timeZoneId = result.data.currentWeatherData?.timeZoneId
                    Log.d("MyLog", "city name: ${city.name}")

                    results[city.name] = ExamplePage(
                        city = city,
                        weatherInfo = result.data,
                        isLoading = false
                    )
                }

                is Result.Error -> {
                    results[city.name] = ExamplePage(
                        city = city,
                        error = result.exception,
                        isLoading = false
                    )
                }

                is Result.Loading -> {
                    if (!results.containsKey(city.name)) {
                        results[city.name] = ExamplePage(city = city, isLoading = true)
                    }
                }
            }
        }
        viewModelState.update { state ->
            state.copy(pagerList = results.values.toList(), isLoading = false)
        }
    }


    private val prefViewModelState = MutableStateFlow(PreferencesState())

    val prefUiState =
        prefViewModelState.stateIn(viewModelScope, SharingStarted.Eagerly, prefViewModelState.value)

    private fun initLoadPref() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!isActive) return@launch
            userPreferencesRepository.preferenceFlow.collect { preferences ->
                prefViewModelState.value = preferences
            }
        }
    }

}