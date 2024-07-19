package com.buller.wweather.domain.repository

import com.buller.wweather.domain.model.PreferencesState
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val preferenceFlow:Flow<PreferencesState>
    suspend fun updatePrefTheme(isDark:Boolean)
    suspend fun updateThemeType(type: Boolean)
    suspend fun updateWindType(type: Boolean)
    suspend fun updatePressureType(type: Boolean)
    suspend fun updatePrecipType(type: Boolean)
    suspend fun updateIsAutoUpdate(type: Boolean)
}