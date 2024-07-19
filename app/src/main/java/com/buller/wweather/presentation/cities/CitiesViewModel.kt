package com.buller.wweather.presentation.cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buller.wweather.domain.repository.RoomRepository
import com.buller.wweather.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val viewModelState =
        MutableStateFlow(CitiesState(cities = emptyList(), isLoading = true))
    val uiState = viewModelState.map(CitiesState::toUiState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    init {
        refreshCities()
    }

    fun refreshCities() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = roomRepository.getCities()
            result.collect { res ->
                viewModelState.update {
                    when (res) {
                        is Result.Success -> it.copy(cities = res.data, isLoading = false)
                        is Result.Error -> it.copy(isLoading = false, errorMessages = res.exception)
                        is Result.Loading -> it.copy(isLoading = true)
                    }
                }
            }
        }
    }
}
