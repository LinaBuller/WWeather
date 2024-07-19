package com.buller.wweather.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentDto(
    @field:Json(name = "cloud")
    val cloud: Int,
    @field:Json(name = "condition")
    val condition: ConditionDto,
    @field:Json(name = "feelslike_c")
    val feelslikeC: Double,
    @field:Json(name = "feelslike_f")
    val feelslikeF: Double,
    @field:Json(name = "gust_kph")
    val gustKph: Double,
    @field:Json(name = "gust_mph")
    val gustMph: Double,
    @field:Json(name = "humidity")
    val humidity: Int,
    @field:Json(name = "is_day")
    val isDay: Int,
    @field:Json(name = "last_updated")
    val lastUpdated: String,
    @field:Json(name = "last_updated_epoch")
    val lastUpdatedEpoch: Int,
    @field:Json(name = "precip_in")
    val precipIn: Double,
    @field:Json(name = "precip_mm")
    val precipMm: Double,
    @field:Json(name = "pressure_in")
    val pressureIn: Double,
    @field:Json(name = "pressure_mb")
    val pressureMb: Double,
    @field:Json(name = "temp_c")
    val tempC: Double,
    @field:Json(name = "temp_f")
    val tempF: Double,
    @field:Json(name = "uv")
    val uv: Int,
    @field:Json(name = "vis_km")
    val visKm: Double,
    @field:Json(name = "vis_miles")
    val visMiles: Double,
    @field:Json(name = "wind_degree")
    val windDegree: Double,
    @field:Json(name = "wind_dir")
    val windDir: String,
    @field:Json(name = "wind_kph")
    val windKph: Double,
    @field:Json(name = "wind_mph")
    val windMph: Double
)