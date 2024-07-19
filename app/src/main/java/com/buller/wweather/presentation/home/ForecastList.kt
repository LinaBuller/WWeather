package com.buller.wweather.presentation.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buller.wweather.domain.model.PreferencesState
import com.buller.wweather.domain.model.WeatherData
import com.buller.wweather.domain.model.WeatherInfo

@Composable
fun ForecastList(
    weatherInfo: WeatherInfo?,
    modifier: Modifier = Modifier,
    prefUiState: PreferencesState
) {
    weatherInfo?.weatherDatePerDay.let { data ->
        if (data != null) {
            Log.d("MyTag", "${data.size}")
            LazyRow(modifier = modifier.fillMaxWidth()) {
                items(data) { dayWeather ->
                    ForecastItem(
                        data = dayWeather,
                        prefUiState = prefUiState,
                        modifier = modifier.fillParentMaxWidth(0.34f)
                    )
                }
            }
        }
    }
}

@Composable
fun ForecastItem(data: WeatherData, prefUiState: PreferencesState, modifier: Modifier = Modifier) {
    val humidity = data.humidity
    val date = data.time
    val iconWeather = data.weatherType.iconRes
    val weatherDesc = data.weatherType.weatherDesc

    val wind: Int
    val maxTemp: Int
    val minTemp: Int
    val windLabel:String

    if (prefUiState.isCelsius) {
        wind = (data.windKph / 3.666).toInt()
        maxTemp = data.maxTempC
        minTemp = data.minTempC
        windLabel = "m/s"
    } else {
        wind = data.windMph.toInt()
        maxTemp = data.maxTempF
        minTemp = data.minTempF
        windLabel = "Mph"
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = date)
        Spacer(modifier.padding(8.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = "$maxTemp°", fontSize = 35.sp, fontWeight = FontWeight.Bold)
            Text(text = "$minTemp°", fontSize = 22.sp)
        }
        Image(
            painter = painterResource(iconWeather),
            contentDescription = weatherDesc,
            modifier = modifier.height(48.dp)
        )
        Box(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 4.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                WeatherDataDisplay(
                    value = wind,
                    unit = windLabel,
                )
                WeatherDataDisplay(
                    value = humidity,
                    unit = "%"
                )
            }
        }
    }
}