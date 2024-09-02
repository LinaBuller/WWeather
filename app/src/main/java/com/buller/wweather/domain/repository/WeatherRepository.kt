package com.buller.wweather.domain.repository

import com.buller.wweather.domain.model.LocationInfo
import com.buller.wweather.domain.util.Result
import com.buller.wweather.domain.model.WeatherInfo
import com.buller.wweather.domain.model.ExamplePage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface WeatherRepository {
    suspend fun getWeatherData(
        q: String,
        days: Int,
        aqi: String,
        alerts: String
    ): Flow<Result<WeatherInfo>>

    suspend fun getSearch(q: String):Flow<Result<List<LocationInfo>>>
    var citiesUpdateEvent: MutableSharedFlow<List<ExamplePage>>
}