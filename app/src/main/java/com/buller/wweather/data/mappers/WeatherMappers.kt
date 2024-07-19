package com.buller.wweather.data.mappers

import android.util.Log
import com.buller.wweather.data.remote.WeatherDto
import com.buller.wweather.domain.model.AstronomyInfo
import com.buller.wweather.domain.model.WeatherData
import com.buller.wweather.domain.model.WeatherInfo
import com.buller.wweather.domain.model.WeatherType


fun WeatherDto.toWeatherDataMap(): Map<Int, List<WeatherData>> {
    val days = mutableMapOf<Int, List<WeatherData>>()

    forecast.forecastday.mapIndexed { indexForecast, forecastdayDto ->
        val list = mutableListOf<WeatherData>()
        forecastdayDto.hour.forEach { hourDto ->

            val time = hourDto.time
            val humidity = hourDto.humidity
            val weatherType = WeatherType.fromWNO(hourDto.condition.code)
            val location = location.name
            val uv = current.uv
            //metric
            val temperatureC = hourDto.tempC
            val windKph = hourDto.windKph
            val maxTempC = forecastdayDto.day.maxTempC
            val minTempC = forecastdayDto.day.minTempC
            //imperial
            val temperatureF = hourDto.tempC
            val windMph = hourDto.windMph
            val maxTempF = forecastdayDto.day.maxTempF
            val minTempF = forecastdayDto.day.minTempF

            val newWeather = WeatherData(
                time = time,
                humidity = humidity,
                weatherType = weatherType,
                location = location,
                uv = uv,
                temperatureC = temperatureC.toInt(),
                windKph = windKph,
                maxTempC = maxTempC.toInt(),
                minTempC = minTempC.toInt(),
                temperatureF = temperatureF.toInt(),
                windMph = windMph,
                maxTempF = maxTempF.toInt(),
                minTempF = minTempF.toInt()
            )
            list.add(newWeather)
        }

        days[indexForecast] = list
        Log.d("MyLog", "$list")
    }
    return days
}

fun WeatherDto.toCurrentWeatherDate(): WeatherData {
    val today = forecast.forecastday[0]
    val time = today.date
    val timestamp = location.localtimeEpoch
    val timeZoneId = location.tzId
    val weatherType = WeatherType.fromWNO(current.condition.code)
    val location = location.name
    val isDay = current.isDay == 1
    val humidity = current.humidity
    val uv = current.uv
    val cloud = current.cloud
    val sunrise = today.astro.sunrise
    val sunset = today.astro.sunset
    val moonrise = today.astro.moonrise
    val moonset = today.astro.moonset
    val moonPhase = today.astro.moonPhase
    //metric
    val temperatureC = current.tempC
    val windKph = current.windKph
    val maxTempC = today.day.maxTempC
    val minTempC = today.day.minTempC
    val feelslikeC = current.feelslikeC
    val pressureMb = current.pressureMb
    val precipMm = current.precipMm
    //imperial
    val temperatureF = current.tempF
    val windMph = current.windMph
    val maxTempF = today.day.maxTempF
    val minTempF = today.day.minTempF
    val feelslikeF = current.feelslikeF
    val pressureIn = current.pressureIn
    val precipIn = current.precipMm

    return WeatherData(
        time = time,
        timestamp = timestamp,
        timeZoneId = timeZoneId,
        weatherType = weatherType,
        location = location,
        isDay = isDay,
        humidity = humidity,
        uv = uv,
        cloud = cloud,
        astroInfo = AstronomyInfo(
            sunrise = sunrise,
            sunset = sunset,
            moonrise = moonrise,
            moonset = moonset,
            moonPhase = moonPhase
        ),

        temperatureC = temperatureC.toInt(),
        windKph = windKph,
        maxTempC = maxTempC.toInt(),
        minTempC = minTempC.toInt(),
        feelslikeC = feelslikeC,
        pressureMb = pressureMb,
        precipMm = precipMm,

        temperatureF = temperatureF.toInt(),
        windMph = windMph,
        maxTempF = minTempF.toInt(),
        minTempF = minTempF.toInt(),
        feelslikeF = feelslikeF,
        pressureIn = pressureIn,
        precipIn = precipIn
    )
}


fun WeatherDto.toWeatherDataPerDay(): List<WeatherData> {
    val list = mutableListOf<WeatherData>()
    forecast.forecastday.forEach {
        val day = it.day
        val time = it.date
        val weatherType = WeatherType.fromWNO(day.condition.code)
        val location = location.name
        val uv = day.uv
        val humidity = day.avgHumidity


        val maxWindKph = day.maxWindKph
        val maxTempC = day.maxTempC
        val minTempC = day.minTempC

        val maxWindMph = day.maxWindMph
        val maxTempF = day.maxTempF
        val minTempF = day.minTempF

        val data = WeatherData(
            time = time,
            weatherType = weatherType,
            location = location,
            uv = uv,
            humidity = humidity,

            temperatureC = 0,
            windKph = maxWindKph,
            maxTempC = maxTempC.toInt(),
            minTempC = minTempC.toInt(),
            temperatureF = 0,
            windMph = maxWindMph,
            maxTempF = maxTempF.toInt(),
            minTempF = minTempF.toInt()
        )
        list.add(data)
    }
    return list
}

fun WeatherDto.toWeatherInfo(): WeatherInfo {
    val weatherDataMap = toWeatherDataMap()
    val currentWeatherData = toCurrentWeatherDate()
    val weatherDataPerDay = toWeatherDataPerDay()
    return WeatherInfo(
        weatherDatePerHour = weatherDataMap,
        currentWeatherData = currentWeatherData,
        weatherDatePerDay = weatherDataPerDay
    )
}