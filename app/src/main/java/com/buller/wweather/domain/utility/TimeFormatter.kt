package com.buller.wweather.domain.utility

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object TimeFormatter {

    fun getShort24HourFormattedTime(timestamp: Int, timeZoneId: String?):String?{
        val instant = Instant.ofEpochSecond(timestamp.toLong())
        val zoneId = ZoneId.of(timeZoneId)
        val zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId)
        return zonedDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    fun getShort12HourFormattedTime(timestamp: Int, timeZoneId: String?):String?{
        val instant = Instant.ofEpochSecond(timestamp.toLong())
        val zoneId = ZoneId.of(timeZoneId)
        val zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId)
        return zonedDateTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
    }
}