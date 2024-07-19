package com.buller.wweather.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore


val Context.userDataStore by preferencesDataStore(
    name = "preview_settings.preferences_datastore"
)