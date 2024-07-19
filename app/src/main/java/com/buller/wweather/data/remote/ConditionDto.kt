package com.buller.wweather.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConditionDto(
    @field:Json(name = "code")
    val code: Int,
    @field:Json(name = "icon")
    val icon: String,
    @field:Json(name = "text")
    val text: String
)