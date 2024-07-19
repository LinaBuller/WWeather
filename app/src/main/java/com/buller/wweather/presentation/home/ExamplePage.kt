package com.buller.wweather.presentation.home

import com.buller.wweather.domain.model.WeatherInfo
import com.buller.wweather.domain.model.City

data class ExamplePage(
    val city: City,
    val weatherInfo: WeatherInfo?=null,
    val isLoading: Boolean = true,
    val error: Throwable? = null
)

