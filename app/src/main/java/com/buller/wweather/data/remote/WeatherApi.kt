package com.buller.wweather.data.remote

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {


    @GET("v1/forecast.json")
    suspend fun getWeatherData(
        @Query("key") key: String,
        @Query("q") city: String,
        @Query("days") days: Int,
        @Query("aqi") aqi: String,
        @Query("alerts") alert: String
    ): WeatherDto
}