package com.buller.wweather.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AstroDto(
    @field:Json(name = "is_moon_up")
    val isMoonUp: Int,
    @field:Json(name = "is_sun_up")
    val isSunUp: Int,
    @field:Json(name = "moon_illumination")
    val moonIllumination: Int,
    @field:Json(name = "moon_phase")
    val moonPhase: String,
    @field:Json(name = "moonrise")
    val moonrise: String,
    @field:Json(name = "moonset")
    val moonset: String,
    @field:Json(name = "sunrise")
    val sunrise: String,
    @field:Json(name = "sunset")
    val sunset: String
)