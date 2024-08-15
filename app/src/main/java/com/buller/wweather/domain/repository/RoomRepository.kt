package com.buller.wweather.domain.repository

import com.buller.wweather.domain.util.Result
import com.buller.wweather.domain.model.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

interface RoomRepository {
    suspend fun getCities(): Flow<Result<List<City>>>
    suspend fun setCity(city: City)
    suspend fun deleteCity()
    suspend fun deleteCities(citiesToDelete: List<City>)
    suspend fun getCityByName(name: String):City?
    val citiesDeletedEvent: MutableSharedFlow<Unit>
}