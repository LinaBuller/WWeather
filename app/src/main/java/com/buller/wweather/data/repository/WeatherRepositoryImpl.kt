package com.buller.wweather.data.repository

import com.buller.wweather.data.mappers.toWeatherInfo
import com.buller.wweather.data.remote.Constants
import com.buller.wweather.data.remote.WeatherApi
import com.buller.wweather.domain.repository.WeatherRepository
import com.buller.wweather.domain.util.Result
import com.buller.wweather.domain.model.WeatherInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val api: WeatherApi) : WeatherRepository {

//    override suspend fun getWeatherData(
//        q: String,
//        days: Int,
//        aqi: String,
//        alerts: String
//    ): Result<WeatherInfo> {
//        return try {
//            val data = api.getWeatherData(Constants.API_KEY, q, days, aqi, alerts)
//            Log.d("MyLog", data.toString())
//            val weatherInfo = data.toWeatherInfo()
//            Log.d("MyLog", "${weatherInfo.currentWeatherData}")
//            Result.Success(
//                data = weatherInfo
//            )
//        } catch (e: Exception) {
//            Log.d("MyLog", "${e.message}")
//            Result.Error(e)
//        }
//    }

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
}