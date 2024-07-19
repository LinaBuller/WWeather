package com.buller.wweather.presentation.search

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buller.wweather.R
import com.buller.wweather.domain.repository.RoomRepository
import com.buller.wweather.domain.util.Result
import com.buller.wweather.domain.model.City
import com.buller.wweather.presentation.cities.CityState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val viewModelState =
        MutableStateFlow(ExistCitiesState(cities = emptyList(), isLoading = true))


    val uiState = viewModelState.map(ExistCitiesState::toUiState).stateIn(
        viewModelScope,
        SharingStarted.Eagerly, viewModelState.value.toUiState()
    )

    fun refreshExistCities(context: Context) {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            viewModelState.update {
                try {
                    val list = mutableListOf<String>()
                    val inputStream =
                        InputStreamReader(context.resources.openRawResource(R.raw.cities))
                    inputStream.forEachLine { cityName ->
                        list.add(cityName)
                    }
                    it.copy(cities = list, isLoading = false)
                } catch (e: IOException) {
                    Log.d("MyLog", e.message.toString())
                    it.copy(cities = emptyList(), isLoading = false)
                }
            }

        }
    }


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

    fun setCityToLocalDatabase(it: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val d = _cities.value.city?.size ?: 0
            val city = if (d == 0) {
                City(name = it, isPin = true, position = d)
            } else {
                City(name = it, position = d)
            }
            roomRepository.setCity(city)
        }
    }

    init {
        loadCities()
    }
}