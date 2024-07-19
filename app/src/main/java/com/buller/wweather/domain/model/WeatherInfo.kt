package com.buller.wweather.domain.model

import com.buller.wweather.domain.model.WeatherData


data class WeatherInfo (
    val weatherDatePerHour: Map<Int, List<WeatherData>>?,
    val currentWeatherData: WeatherData?,
    val weatherDatePerDay:List<WeatherData>
)