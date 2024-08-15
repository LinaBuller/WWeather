package com.buller.wweather.domain.model

data class LocationInfo(
    val id: Int,
    val name: String = "",
    val region: String = "",
    val country: String = ""
)