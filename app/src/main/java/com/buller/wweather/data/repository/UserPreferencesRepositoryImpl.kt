package com.buller.wweather.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.buller.wweather.domain.model.PreferencesState
import com.buller.wweather.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(private val dataStoreRepository: DataStore<Preferences>) :
    UserPreferencesRepository {


    override val preferenceFlow = dataStoreRepository.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val prefTheme = preferences[FORCE_DARK_THEME] ?: false
        val tempUnitType = preferences[TYPE_TEMPERATURE] ?: false
        val precipType = preferences[TYPE_PRECIP] ?: false
        val pressureType = preferences[TYPE_PRESSURE] ?: false
        val windType = preferences[TYPE_WIND] ?: false
        val isAutoUpdate = preferences[IS_AUTO_UPDATE] ?: false
        PreferencesState(
            isTheme = prefTheme,
            isAutoUpdate = isAutoUpdate,
            isMetricPressureType = pressureType,
            isMetricPrecipType = precipType,
            isMetricWindType = windType,
            isCelsius = tempUnitType
        )
    }

    private companion object {
        val TYPE_TEMPERATURE = booleanPreferencesKey(name = "type_temperature")
        val FORCE_DARK_THEME = booleanPreferencesKey(name = "force_dark_theme")
        val TYPE_PRECIP = booleanPreferencesKey(name = "type_precipitation")
        val TYPE_PRESSURE = booleanPreferencesKey(name = "type_pressure")
        val TYPE_WIND = booleanPreferencesKey(name = "type_wind")
        val IS_AUTO_UPDATE = booleanPreferencesKey(name = "is_auto_update")
    }

    override suspend fun updatePrefTheme(isDark: Boolean) {
        dataStoreRepository.edit { preferences ->
            preferences[FORCE_DARK_THEME] = isDark
        }
    }

    override suspend fun updateThemeType(type: Boolean) {
        dataStoreRepository.edit { preferences ->
            preferences[TYPE_TEMPERATURE] = type
        }
    }

    override suspend fun updateWindType(type: Boolean) {
        dataStoreRepository.edit { preferences ->
            preferences[TYPE_WIND] = type
        }
    }

    override suspend fun updatePressureType(type: Boolean) {
        dataStoreRepository.edit { preferences ->
            preferences[TYPE_PRESSURE] = type
        }
    }

    override suspend fun updatePrecipType(type: Boolean) {
        dataStoreRepository.edit { preferences ->
            preferences[TYPE_PRECIP] = type
        }
    }

    override suspend fun updateIsAutoUpdate(type: Boolean) {
        dataStoreRepository.edit { preferences ->
            preferences[IS_AUTO_UPDATE] = type
        }
    }
}