package com.buller.wweather.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ForecastDto(
    @field:Json(name = "forecastday")
    val forecastday: List<ForecastdayDto>
)