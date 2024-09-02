package com.buller.wweather.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buller.wweather.R
import com.buller.wweather.domain.model.PreferencesState
import com.buller.wweather.domain.model.WeatherInfo
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun WeatherCard(
    modifier: Modifier = Modifier,
    weatherInfo: WeatherInfo?,
    prefUiState: PreferencesState
) {
    weatherInfo?.currentWeatherData?.let { data ->
        val iconRes = data.weatherType.iconRes
        val weatherDesc = data.weatherType.weatherDesc
        val timestamp = data.timestamp
        val timeZoneId = data.timeZoneId
        val zoneId = ZoneId.of(timeZoneId)
        val local = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp.toLong()), zoneId)
        val dateFormat24h = DateTimeFormatter.ofPattern("HH:mm — EEEE, dd MMMM yyyy")
        //TODO do 12h time format
        val dateFormat12h = DateTimeFormatter.ofPattern("hh:mm a — EEEE, dd MMMM yyyy")
        val currentTime = local.format(dateFormat24h)

        val temp: Int
        val minTemp: Int
        val maxTemp: Int
        val feelslike: Double

        if (prefUiState.isCelsius) {
            //metric
            temp = data.temperatureC
            minTemp = data.minTempC
            maxTemp = data.maxTempC
            feelslike = data.feelslikeC
        } else {
            //imperial
            temp = data.temperatureF
            minTemp = data.minTempF
            maxTemp = data.maxTempF
            feelslike = data.feelslikeF
        }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    text = currentTime
                )
            }
            Spacer(modifier = modifier.padding(80.dp))
            Text(
                text = "${temp}°",
                fontSize = 100.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Image(
                    painter = painterResource(iconRes),
                    modifier = modifier.height(48.dp),
                    contentDescription = ""
                )
                Text(
                    text = weatherDesc,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

            }
            Spacer(modifier = modifier.height(16.dp))
            Text(
                text = "$maxTemp° / $minTemp° " + stringResource(R.string.feelslike)
                        + " ${feelslike.toInt()}°",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}