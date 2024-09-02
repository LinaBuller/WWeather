package com.buller.wweather.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buller.wweather.domain.model.PartOfCurWeather
import com.buller.wweather.domain.model.PreferencesState
import com.buller.wweather.domain.model.WeatherInfo

@Composable
fun WeatherInfoList(
    modifier: Modifier = Modifier,
    weatherInfo: WeatherInfo?,
    prefUiState: PreferencesState,

) {
    weatherInfo?.currentWeatherData?.let { data ->

        val wind:String
        var windElement: PartOfCurWeather =
            PartOfCurWeather.getPartOfWeather(PartOfCurWeather.METRIC_WIND_ELM)
        if (prefUiState.isMetricWindType) {
            val ms = (data.windKph / 3.666).toInt()
            wind = ms.toString()
        } else {
            wind = data.windMph.toString()
            windElement = PartOfCurWeather.getPartOfWeather(PartOfCurWeather.IMPERIAL_WIND_ELM)
        }

        val pressure:String
        var pressureElement: PartOfCurWeather =
            PartOfCurWeather.getPartOfWeather(PartOfCurWeather.METRIC_PRESSURE_ELM)
        if (prefUiState.isMetricPressureType) {
            pressure = data.pressureMb.toString()
        } else {
            pressure = data.pressureIn.toString()
            pressureElement =
                PartOfCurWeather.getPartOfWeather(PartOfCurWeather.IMPERIAL_PRESSURE_ELM)
        }
        val precip:String
        var precipElement: PartOfCurWeather =
            PartOfCurWeather.getPartOfWeather(PartOfCurWeather.METRIC_PRECIP_ELM)
        if (prefUiState.isMetricPrecipType) {
            precip = data.precipMm.toString()
        } else {
            precip = data.precipIn.toString()
            precipElement = PartOfCurWeather.getPartOfWeather(PartOfCurWeather.IMPERIAL_PRECIP_ELM)
        }

        val uv =  data.uv.toString()
        val uvElement = PartOfCurWeather.getPartOfWeather(PartOfCurWeather.UV_ELM)

        val humidity =  data.humidity.toString()
        val humidityElement = PartOfCurWeather.getPartOfWeather(PartOfCurWeather.HUMIDITY_ELM)

        val cloud =  data.cloud.toString()
        val cloudElement = PartOfCurWeather.getPartOfWeather(PartOfCurWeather.CLOUD_ELM)

        Column(modifier = modifier.fillMaxWidth()) {
            Row(modifier = modifier.fillMaxWidth()) {
                Box(
                    modifier = modifier
                        .weight(1f)
                        .padding(bottom = 8.dp)
                ) {
                    WeatherItem(
                        element = windElement,
                        value = wind,
                        modifier = modifier
                    )
                }

                Box(
                    modifier = modifier
                        .weight(1f)
                        .padding(bottom = 8.dp, end = 8.dp)
                ) {
                    WeatherItem(
                        element = uvElement,
                        value = uv,
                        modifier = modifier
                    )
                }
                Box(
                    modifier = modifier
                        .weight(1f)
                        .padding(bottom = 8.dp, end = 8.dp)
                ) {
                    WeatherItem(
                        element = humidityElement,
                        value = humidity,
                        modifier = modifier
                    )
                }
            }

            Row(modifier = modifier.fillMaxWidth()) {
                Box(
                    modifier = modifier
                        .weight(1f)
                        .padding(bottom = 8.dp)
                ) {
                    WeatherItem(
                        element =
                        pressureElement,
                        value = pressure,
                        modifier = modifier
                    )
                }
                Box(modifier = modifier.weight(1f)) {
                    WeatherItem(
                        element = cloudElement,
                        value = cloud,
                        modifier = modifier
                    )
                }
                Box(
                    modifier = modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    WeatherItem(
                        element = precipElement,
                        value = precip,
                        modifier = modifier
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherItem(element: PartOfCurWeather, value: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(text = element.weatherDesc)
        Text(text = value, modifier = modifier.padding(top = 8.dp), fontSize = 25.sp)
        Text(text = element.unit)
    }
}