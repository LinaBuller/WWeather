package com.buller.wweather.presentation.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buller.wweather.domain.repository.RoomRepository
import com.buller.wweather.domain.util.Result
import com.buller.wweather.domain.model.City
import com.buller.wweather.domain.model.CityState
import com.buller.wweather.domain.model.LocationInfo
import com.buller.wweather.domain.model.LocationState
import com.buller.wweather.domain.model.SearchState
import com.buller.wweather.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _cities: MutableState<CityState> = mutableStateOf(CityState())
    val cities: State<CityState> = _cities

    private fun loadCities() {
        viewModelScope.launch {
            val result = roomRepository.getCities()
            result.collect { res ->
                when (res) {
                    is Result.Success -> {
                        _cities.value = CityState(city = res.data, isLoading = false, error = null)
                    }

                    is Result.Error -> {
                        _cities.value =
                            CityState(city = null, isLoading = false, error = res.exception)
                    }

                    is Result.Loading -> {
                        _cities.value = CityState(city = null, isLoading = true, error = null)
                    }
                }
            }
        }
    }

    fun setCityToLocalDatabase(locationInfo: LocationInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            val position = _cities.value.city?.size ?: 0
            val city = City(
                    id = locationInfo.id,
                    name = locationInfo.name,
                    region = locationInfo.region,
                    country = locationInfo.country,
                    isPin = true,
                    position = position
                )
            roomRepository.setCity(city)
        }
    }

    private val searchQuery = MutableStateFlow("")

    fun emitSearchText(text: String) {
        viewModelScope.launch {
            searchQuery.emit(text)
        }
    }

    init {
        loadCities()
        setDebounceSearchRequest()
    }

    private val searchViewModelState =
        MutableStateFlow(SearchState(locationList = emptyList()))

    val searchUiState: StateFlow<LocationState> =
        searchViewModelState.map(SearchState::toUiState).stateIn(
            viewModelScope,
            SharingStarted.Eagerly, searchViewModelState.value.toUiState()
        )

    private fun getSearchLocations(q: String) {
        searchViewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = repository.getSearch(q)
            result.collect { res ->
                searchViewModelState.update {
                    when (res) {
                        is Result.Success -> {
                            it.copy(locationList = res.data, isLoading = false)
                        }

                        is Result.Error -> {
                            it.copy(
                                locationList = emptyList(),
                                isLoading = false,
                                error = res.exception
                            )
                        }

                        is Result.Loading -> {
                            it.copy(locationList = emptyList(), isLoading = true)
                        }
                    }
                }
            }
        }
    }

    private fun setDebounceSearchRequest() {
        viewModelScope.launch {
            searchQuery.debounce(300).collect { query ->
                if (query.isNotBlank()) {
                    getSearchLocations(query)
                }
            }
        }
    }

    fun refreshSearchRequest() {
        setDebounceSearchRequest()
    }

}