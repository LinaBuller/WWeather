package com.buller.wweather.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buller.wweather.domain.model.PreferencesState
import com.buller.wweather.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val prefViewModelState = MutableStateFlow(PreferencesState())

    val prefUiState =
        prefViewModelState.stateIn(viewModelScope, SharingStarted.Eagerly, prefViewModelState.value)

    init {
        viewModelScope.launch {
            userPreferencesRepository.preferenceFlow.collect { preferences ->
                prefViewModelState.value = preferences
            }
        }
    }

    fun updatePreferTheme(themeType: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updatePrefTheme(themeType)
        }
    }

    fun updateTempType(type: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateTempType(type)
        }
    }

    fun updateWindType(type: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateWindType(type)
        }
    }

    fun updatePressureType(type: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updatePressureType(type)
        }
    }

    fun updatePrecipType(type: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updatePrecipType(type)
        }
    }

    fun updateIsAutoUpdate(autoUpdate: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateIsAutoUpdate(autoUpdate)
        }
    }
}