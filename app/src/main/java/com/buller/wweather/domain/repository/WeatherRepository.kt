package com.buller.wweather.domain.repository

import com.buller.wweather.domain.util.Result
import com.buller.wweather.domain.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherData(
        q: String,
        days: Int,
        aqi: String,
        alerts: String
    ): Flow<Result<WeatherInfo>>
}