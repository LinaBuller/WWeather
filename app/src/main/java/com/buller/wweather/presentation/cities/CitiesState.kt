package com.buller.wweather.presentation.cities

import com.buller.wweather.domain.model.City

sealed interface CitiesUiState {
    val isLoading: Boolean
    val errorMessages: Throwable?

    data class NoCities(override val isLoading: Boolean, override val errorMessages: Throwable?) :
        CitiesUiState

    data class HasCities(
        val cities: List<City>, override val isLoading: Boolean,
        override val errorMessages: Throwable?
    ) : CitiesUiState
}

data class CitiesState(
    val cities: List<City>,
    val isLoading: Boolean = false,
    val errorMessages: Throwable? = null
) {
    fun toUiState(): CitiesUiState =
        if (cities.isEmpty()) {
            CitiesUiState.NoCities(isLoading = isLoading, errorMessages = errorMessages)
        } else {
            CitiesUiState.HasCities(
                cities = cities,
                isLoading = isLoading,
                errorMessages = errorMessages
            )
        }
}