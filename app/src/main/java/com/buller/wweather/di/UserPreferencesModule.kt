package com.buller.wweather.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.buller.wweather.data.repository.UserPreferencesRepositoryImpl
import com.buller.wweather.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserPreferencesModule {
    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(userPreferencesRepository: UserPreferencesRepositoryImpl)
    : UserPreferencesRepository

    companion object {
        @Provides
        @Singleton
        fun provideUserDataStorePreferences(@ApplicationContext applicationContext: Context)
        : DataStore<Preferences> {
            return applicationContext.userDataStore
        }
    }
}