package com.buller.wweather.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationDto(
    @field:Json(name = "country")
    val country: String,
    @field:Json(name = "lat")
    val lat: Double,
    @field:Json(name = "localtime")
    val localtime: String,
    @field:Json(name = "localtime_epoch")
    val localtimeEpoch: Int,
    @field:Json(name = "lon")
    val lon: Double,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "region")
    val region: String,
    @field:Json(name = "tz_id")
    val tzId: String
)