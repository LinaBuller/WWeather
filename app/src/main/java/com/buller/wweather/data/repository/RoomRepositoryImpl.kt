package com.buller.wweather.data.repository


import com.buller.wweather.data.room.WeatherDao
import com.buller.wweather.domain.repository.RoomRepository
import com.buller.wweather.domain.util.Result
import com.buller.wweather.domain.util.asResult
import com.buller.wweather.domain.model.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    val dao: WeatherDao
) : RoomRepository {

    override val citiesDeletedEvent = MutableSharedFlow<Unit>()

    override suspend fun getCities(): Flow<Result<List<City>>> = dao.getCities().asResult()

    override suspend fun setCity(city: City) = dao.setCity(city = city)


    override suspend fun deleteCity() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCities(citiesToDelete: List<City>) {
        dao.deleteCities(citiesToDelete)
    }

    override suspend fun getCityByName(name: String): City? {
        return dao.getCityByName(name)
    }
}