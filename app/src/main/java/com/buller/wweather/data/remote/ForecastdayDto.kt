package com.buller.wweather.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ForecastdayDto(
    @field:Json(name = "astro")
    val astro: AstroDto,
    @field:Json(name = "date")
    val date: String,
    @field:Json(name = "date_epoch")
    val dateEpoch: Int,
    @field:Json(name = "day")
    val day: DayDto,
    @field:Json(name = "hour")
    val hour: List<HourDto>
)