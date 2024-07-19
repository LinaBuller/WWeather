package com.buller.wweather.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherDto(
    @field:Json(name = "location")
    val location: LocationDto,
    @field:Json(name = "current")
    val current: CurrentDto,
    @field:Json(name = "forecast")
    val forecast: ForecastDto,
)