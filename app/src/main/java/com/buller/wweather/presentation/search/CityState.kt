package com.buller.wweather.presentation.search

import com.buller.wweather.domain.model.City

data class CityState(
    val city: List<City>? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null
)