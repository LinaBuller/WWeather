package com.buller.wweather.domain.model

import androidx.annotation.DrawableRes
import com.buller.wweather.R

sealed class PartOfCurWeather(
    val weatherDesc: String,
    @DrawableRes val iconRes: Int,val unit:String
) {
    //TODO change icon
    data object HumidityCard : PartOfCurWeather(weatherDesc = "Humidity", R.drawable.mist_day_icon,"%")
    data object UVCard : PartOfCurWeather(weatherDesc = "UV", R.drawable.mist_day_icon,"")
    data object CloudCard : PartOfCurWeather(weatherDesc = "Cloud", R.drawable.mist_day_icon,"%")

    //metric
    data object MetricWindCard : PartOfCurWeather(weatherDesc = "Wind", R.drawable.mist_day_icon,"m/s")
    data object MetricPressureCard : PartOfCurWeather(weatherDesc = "Pressure", R.drawable.mist_day_icon,"mm")
    data object MetricPrecipCard : PartOfCurWeather(weatherDesc = "Precipitation", R.drawable.mist_day_icon,"mm")

    //imperial
    data object ImperialWindCard : PartOfCurWeather(weatherDesc = "Wind", R.drawable.mist_day_icon,"mph")
    data object ImperialPressureCard : PartOfCurWeather(weatherDesc = "Pressure", R.drawable.mist_day_icon,"in")
    data object ImperialPrecipCard : PartOfCurWeather(weatherDesc = "Precipitation", R.drawable.mist_day_icon,"in")

    companion object {
        const val UV_ELM = 2222
        const val HUMIDITY_ELM = 4444
        const val CLOUD_ELM = 6666

        const val METRIC_WIND_ELM = 1111
        const val METRIC_PRESSURE_ELM = 3333
        const val METRIC_PRECIP_ELM = 5555

        const val IMPERIAL_WIND_ELM = 7777
        const val IMPERIAL_PRESSURE_ELM = 8888
        const val IMPERIAL_PRECIP_ELM = 9999

        fun getPartOfWeather(code: Int): PartOfCurWeather {
            return when (code) {
                UV_ELM -> UVCard
                HUMIDITY_ELM -> HumidityCard
                CLOUD_ELM -> CloudCard

                METRIC_WIND_ELM -> MetricWindCard
                METRIC_PRESSURE_ELM -> MetricPressureCard
                METRIC_PRECIP_ELM -> MetricPrecipCard

                IMPERIAL_WIND_ELM->ImperialWindCard
                IMPERIAL_PRESSURE_ELM -> ImperialPressureCard
                IMPERIAL_PRECIP_ELM -> ImperialPrecipCard
                else -> HumidityCard
            }
        }
    }
}