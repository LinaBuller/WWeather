package com.buller.wweather.domain.location

import android.location.Location


interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}