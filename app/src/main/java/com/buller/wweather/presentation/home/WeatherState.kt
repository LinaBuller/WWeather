package com.buller.wweather.presentation.home

import com.buller.wweather.domain.model.WeatherInfo


sealed interface HomeUiState {
    val isLoading: Boolean
    val errorMessages: Throwable?

    data class NoInfo(
        override val isLoading: Boolean,
        override val errorMessages: Throwable?
    ) : HomeUiState

    data class HasInfo(
        val weatherInfo: WeatherInfo,
        override val isLoading: Boolean,
        override val errorMessages: Throwable?
    ) : HomeUiState
}

data class WeatherState(
    val weatherInfo: WeatherInfo?,
    val isLoading: Boolean = false,
    val error: Throwable? = null
) {
    fun toUiState(): HomeUiState =
        if (weatherInfo == null) {
            HomeUiState.NoInfo(
                isLoading = isLoading,
                errorMessages = error
            )
        } else {
            HomeUiState.HasInfo(
                weatherInfo = weatherInfo,
                isLoading = isLoading,
                errorMessages = error
            )
        }
}