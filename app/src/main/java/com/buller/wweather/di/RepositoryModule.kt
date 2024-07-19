package com.buller.wweather.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.buller.wweather.data.location.DefaultLocationTracker
import com.buller.wweather.data.repository.RoomRepositoryImpl
import com.buller.wweather.data.repository.WeatherRepositoryImpl
import com.buller.wweather.data.room.DatabaseConstants
import com.buller.wweather.data.room.LocalDatabase
import com.buller.wweather.domain.location.LocationTracker
import com.buller.wweather.domain.repository.RoomRepository
import com.buller.wweather.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository

}