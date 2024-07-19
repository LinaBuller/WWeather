package com.buller.wweather.domain.model

data class WeatherData(
    val timestamp: Int = 0,
    val timeZoneId: String = "",
    val location: String,
    val time: String,
    val humidity: Int,
    val weatherType: WeatherType,
    val uv: Int,
    val isDay: Boolean? = null,
    val cloud: Int = 0,
    val astroInfo: AstronomyInfo? = null,


    //metric
    val temperatureC: Int,
    val maxTempC: Int,
    val minTempC: Int,
    val windKph: Double,
    val feelslikeC: Double = 0.0,
    val pressureMb: Double? = null,
    val precipMm: Double? = null,

    //imperial
    val temperatureF: Int,
    val maxTempF: Int,
    val minTempF: Int,
    val windMph: Double,
    val feelslikeF: Double = 0.0,
    val pressureIn: Double? = null,
    val precipIn: Double? = null,

    )