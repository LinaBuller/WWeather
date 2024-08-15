package com.buller.wweather.data.repository

import com.buller.wweather.data.mappers.toLocationInfo
import com.buller.wweather.data.mappers.toWeatherInfo
import com.buller.wweather.data.remote.Constants
import com.buller.wweather.data.remote.WeatherApi
import com.buller.wweather.domain.model.LocationInfo
import com.buller.wweather.domain.repository.WeatherRepository
import com.buller.wweather.domain.util.Result
import com.buller.wweather.domain.model.WeatherInfo
import com.buller.wweather.presentation.home.ExamplePage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val api: WeatherApi,

) : WeatherRepository {
    override var citiesUpdateEvent: MutableSharedFlow<List<ExamplePage>> = MutableSharedFlow()

    override suspend fun getWeatherData(
        q: String,
        days: Int,
        aqi: String,
        alerts: String
    ): Flow<Result<WeatherInfo>> = flow {
        emit(Result.Loading)
        val data = api.getWeatherData(Constants.API_KEY, q, days, aqi, alerts)
        val weatherInfo = data.toWeatherInfo()
        emit(Result.Success(data = weatherInfo))
    }.catch { e ->
        emit(Result.Error(e))
    }

    override suspend fun getSearch(q: String): Flow<Result<List<LocationInfo>>> = flow {
        emit(Result.Loading)
        val data = api.getLocationInfo(Constants.API_KEY, q)
        val locationInfo = data.map {
            it.toLocationInfo()
        }
        emit(Result.Success(data = locationInfo))
    }.catch { e ->
        emit(Result.Error(e))
    }
}