package com.buller.wweather.domain.repository

import com.buller.wweather.domain.util.Result
import com.buller.wweather.domain.model.City
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    fun getCities(): Flow<Result<List<City>>>
    fun setCity(city: City)
    fun deleteCity()
}