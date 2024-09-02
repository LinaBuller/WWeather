package com.buller.wweather.domain.model

import com.buller.wweather.domain.model.WeatherInfo
import com.buller.wweather.domain.model.City

data class ExamplePage(
    val city: City,
    val weatherInfo: WeatherInfo?=null,
    val isLoading: Boolean = true,
    val error: Throwable? = null
)

