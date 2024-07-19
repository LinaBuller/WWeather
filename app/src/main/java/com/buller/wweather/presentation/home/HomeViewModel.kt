package com.buller.wweather.presentation.home

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buller.wweather.domain.location.LocationTracker
import com.buller.wweather.domain.repository.RoomRepository
import com.buller.wweather.domain.repository.WeatherRepository
import com.buller.wweather.domain.util.Result
import com.buller.wweather.domain.model.WeatherInfo
import com.buller.wweather.domain.model.City
import com.buller.wweather.domain.model.PreferencesState
import com.buller.wweather.domain.repository.UserPreferencesRepository
import com.buller.wweather.presentation.cities.CitiesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val roomRepository: RoomRepository,
    private val locationTracker: LocationTracker,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val citiesViewModelState =
        MutableStateFlow(CitiesState(cities = emptyList(), isLoading = true))

    val citiesUiState = citiesViewModelState.map(CitiesState::toUiState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, citiesViewModelState.value.toUiState())

    fun setWeatherForCity(name: String) {
        refreshMainWeather()
    }

    init {
        refreshCities()
        initLoadPref()
    }

    private val viewModelState = MutableStateFlow(PagerState(pagerList = emptyList()))

    val uiState = viewModelState.map(PagerState::toUiState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun refreshMainWeather() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            locationTracker.getCurrentLocation()?.let { location ->
                val savedCities = citiesViewModelState.value.cities
                if (savedCities.isEmpty()) {
                    loadCityFromLocation(location)
                } else {
                    loadCitiesFromLocalDatabase(savedCities)
                }
            }
        }
    }

    private fun setCityInLocalDatabase(data: WeatherInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            if (data.currentWeatherData != null) {
                roomRepository.setCity(
                    getCityFromWeatherInfo(data)
                )
            }
        }
    }

    private fun getCityFromWeatherInfo(data: WeatherInfo): City {
        return City(name = data.currentWeatherData?.location ?: "", isPin = true, position = 0)
    }

    fun refreshCities() {
        citiesViewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = roomRepository.getCities()
            result.collect { res ->
                citiesViewModelState.update {
                    when (res) {
                        is Result.Success -> it.copy(
                            cities = res.data,
                            isLoading = false
                        )

                        is Result.Error -> it.copy(
                            isLoading = false,
                            errorMessages = res.exception
                        )

                        is Result.Loading -> it.copy(isLoading = true)
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
            viewModelState.update {
                when (res) {
                    is Result.Success -> {
                        setCityInLocalDatabase(res.data)
                        val city = getCityFromWeatherInfo(res.data)
                        val weatherInfo = res.data
                        val page = ExamplePage(
                            city = city,
                            weatherInfo = weatherInfo,
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

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun loadCitiesFromLocalDatabase(cities: List<City>) {
        flowOf(cities.first()).flatMapMerge(concurrency = 5) { firstCity ->
            flow {
                emit(firstCity to Result.Loading)
                repository.getWeatherData(
                    q = firstCity.name,
                    days = 3,
                    aqi = "no",
                    alerts = "no"
                ).collect { result ->
                    emit(firstCity to result)
                }

                cities.drop(1).forEach { city ->
                    repository.getWeatherData(
                        q = city.name, days = 3, aqi = "no", alerts = "no"
                    ).collect { result ->
                        emit(city to result)
                    }
                }
            }
        }.collect { (city, result) ->
            viewModelState.update { state ->
                val newList = state.pagerList?.toMutableList() ?: mutableListOf()
                when (result) {
                    is Result.Success -> {
                        val index =
                            newList.indexOfFirst { it.city.name == city.name }
                        if (index != -1) {
                            val oldPage = newList[index]
                            val newPage = oldPage.copy(
                                weatherInfo = result.data,
                                isLoading = false
                            )
                            newList[index] = newPage
                        } else {
                            val newPage = ExamplePage(
                                city = city,
                                weatherInfo = result.data,
                                isLoading = false
                            )
                            newList.add(newPage)
                        }
                        state.copy(pagerList = newList, isLoading = false)
                    }

                    is Result.Error -> {

                        val index =
                            newList.indexOfFirst { it.city.name == city.name }
                        if (index != -1) {
                            val oldPage = newList[index]
                            val newPage = oldPage.copy(
                                isLoading = false,
                                error = result.exception,
                            )
                            newList[index] = newPage
                        } else {
                            val newPage = ExamplePage(
                                city = city,
                                error = result.exception,
                                isLoading = false
                            )
                            newList.add(newPage)
                        }
                        state.copy(
                            pagerList = newList,
                            error = result.exception,
                            isLoading = false,
                        )
                    }

                    is Result.Loading -> {
                        val index =
                            newList.indexOfFirst { it.city.name == city.name }
                        if (index != -1) {
                            val oldPage = newList[index]
                            val newPage = oldPage.copy(
                                isLoading = true
                            )
                            newList[index] = newPage
                        } else {
                            val newPage = ExamplePage(
                                city = city,
                                isLoading = true
                            )
                            newList.add(newPage)
                        }
                        state.copy(pagerList = newList, isLoading = true)
                    }
                }
            }
        }
    }

    private val prefViewModelState = MutableStateFlow(PreferencesState())

    val prefUiState =
        prefViewModelState.stateIn(viewModelScope, SharingStarted.Eagerly, prefViewModelState.value)

    private fun initLoadPref(){
        viewModelScope.launch {
            userPreferencesRepository.preferenceFlow.collect { preferences ->
                prefViewModelState.value = preferences
            }
        }
    }

}