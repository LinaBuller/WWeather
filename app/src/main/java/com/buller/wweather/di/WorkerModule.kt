package com.buller.wweather.di

import android.content.Context
import com.buller.wweather.data.repository.WorkManagerRepositoryImpl
import com.buller.wweather.domain.repository.RoomRepository
import com.buller.wweather.domain.repository.WeatherRepository
import com.buller.wweather.domain.repository.WorkManagerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkerModule {
    @Binds
    @Singleton
    abstract fun bindWorkManagerRepository(
        workManagerRepositoryImpl: WorkManagerRepositoryImpl
    ): WorkManagerRepository

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WorkerEntryPoint {
        fun weatherRepository(): WeatherRepository
        fun roomRepository(): RoomRepository
    }
}