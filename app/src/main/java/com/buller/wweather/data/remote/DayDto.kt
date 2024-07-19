package com.buller.wweather.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DayDto(
    @field:Json(name = "avghumidity")
    val avgHumidity: Int,
    @field:Json(name = "avgtemp_c")
    val avgTempC: Double,
    @field:Json(name = "avgtemp_f")
    val avgTempF: Double,
    @field:Json(name = "avgvis_km")
    val avgVisKm: Double,
    @field:Json(name = "avgvis_miles")
    val avgVisMiles: Int,
    @field:Json(name = "condition")
    val condition: ConditionDto,
    @field:Json(name = "daily_chance_of_rain")
    val dailyChanceOfRain: Int,
    @field:Json(name = "daily_chance_of_snow")
    val dailyChanceOfSnow: Int,
    @field:Json(name = "daily_will_it_rain")
    val dailyWillItRain: Int,
    @field:Json(name = "daily_will_it_snow")
    val dailyWillItSnow: Int,
    @field:Json(name = "maxtemp_c")
    val maxTempC: Double,
    @field:Json(name = "maxtemp_f")
    val maxTempF: Double,
    @field:Json(name = "maxwind_kph")
    val maxWindKph: Double,
    @field:Json(name = "maxwind_mph")
    val maxWindMph: Double,
    @field:Json(name = "mintemp_c")
    val minTempC: Double,
    @field:Json(name = "mintemp_f")
    val minTempF: Double,
    @field:Json(name = "totalprecip_in")
    val totalPrecipIn: Double,
    @field:Json(name = "totalprecip_mm")
    val totalPrecipMm: Double,
    @field:Json(name = "totalsnow_cm")
    val totalSnowCm: Double,
    @field:Json(name = "uv")
    val uv: Int
)