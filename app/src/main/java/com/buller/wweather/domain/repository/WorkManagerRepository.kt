package com.buller.wweather.domain.repository

import androidx.lifecycle.LifecycleOwner

interface WorkManagerRepository {
    fun fetchWeather(lifecycleOwner: LifecycleOwner)
}