package com.buller.wweather.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.buller.wweather.domain.model.City
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM ${DatabaseConstants.CITY_TABLE_NAME}")
    fun getCities(): Flow<List<City>>

    @Insert
    fun setCity(city: City)

    @Delete
    fun deleteCity(city: City)
}