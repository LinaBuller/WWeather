package com.buller.wweather.presentation.search

sealed interface SearchUiState {
    val isLoading: Boolean
    val errorMessages: Throwable?

    data class NoExistCities(
        override val isLoading: Boolean,
        override val errorMessages: Throwable?
    ) : SearchUiState

    data class HasExistCities(
        val cities: List<String>,
        override val isLoading: Boolean,
        override val errorMessages: Throwable?
    ) : SearchUiState
}

data class ExistCitiesState(
    val cities: List<String>,
    val isLoading: Boolean = false,
    val errorMessages: Throwable? = null
) {
    fun toUiState(): SearchUiState =
        if (cities.isEmpty()) {
            SearchUiState.NoExistCities(isLoading = isLoading, errorMessages = errorMessages)
        } else {
            SearchUiState.HasExistCities(
                cities = cities,
                isLoading = isLoading,
                errorMessages = errorMessages
            )
        }
}