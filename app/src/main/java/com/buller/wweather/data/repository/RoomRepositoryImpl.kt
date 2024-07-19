package com.buller.wweather.data.repository


import com.buller.wweather.data.room.WeatherDao
import com.buller.wweather.domain.repository.RoomRepository
import com.buller.wweather.domain.util.Result
import com.buller.wweather.domain.util.asResult
import com.buller.wweather.domain.model.City
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(val dao: WeatherDao) : RoomRepository {

    override fun getCities(): Flow<Result<List<City>>> = dao.getCities().asResult()

    override fun setCity(city: City) = dao.setCity(city = city)


    override fun deleteCity() {
        TODO("Not yet implemented")
    }
}