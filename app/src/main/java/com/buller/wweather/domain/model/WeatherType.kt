package com.buller.wweather.domain.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.buller.wweather.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class WeatherType(val weatherDesc: String, @DrawableRes val iconRes: Int) : Parcelable {
    data object ClearSky :
        WeatherType(weatherDesc = "Clear sky", iconRes = R.drawable.sunny_clear_day_icon)

    data object PartlyCloudy :
        WeatherType(weatherDesc = "Partly cloudy", iconRes = R.drawable.partly_cloudy_day_icon)

    data object Overcast :
        WeatherType(weatherDesc = "Overcast", iconRes = R.drawable.cloudy_icon)

    data object Foggy :
        WeatherType(weatherDesc = "Foggy", iconRes = R.drawable.fog_icon)

    data object Thunder :
        WeatherType(weatherDesc = "Thunder", iconRes = R.drawable.thunder__day_icon)

    data object RainPossible :
        WeatherType(weatherDesc = "Rain possible", iconRes = R.drawable.rain_possible_day_icon)

    data object ModerateRain :
        WeatherType(weatherDesc = "Moderate rain", iconRes = R.drawable.moderate_rain_icon)

    data object Mist :
        WeatherType(weatherDesc = "Mist", iconRes = R.drawable.mist_day_icon)

    data object DrizzleRain :
        WeatherType(weatherDesc = "Drizzle rain", iconRes = R.drawable.drizzle_rain_icon)

    data object HeavyRain :
        WeatherType(weatherDesc = "Heavy rain", iconRes = R.drawable.heavy_rain_icon)

    data object LightSnow :
        WeatherType(weatherDesc = "Light snow", iconRes = R.drawable.light_snow_icon)

    data object SnowPossible :
        WeatherType(weatherDesc = "Snow possible", iconRes = R.drawable.snow_posible_day_icon)

    companion object {
        fun fromWNO(code: Int): WeatherType {
            return when (code) {
                1000 -> ClearSky
                1003 -> PartlyCloudy
                //1006->Cloudy
                1009 -> Overcast
                1030 -> Mist
                1063 -> RainPossible
                1066 -> SnowPossible
                //1069->SleetPossible
                //1072->Patchy freezing drizzle possible
                1087 -> Thunder
                //1114->Blowing snow
                //1117->Blizzard
                1135 -> Foggy
                //1147->Freezing fog
                //1150->Patchy light drizzle
                //1153->Light drizzle
                //1168->Freezing drizzle
                //1171->Heavy freezing drizzle
                //1171->Heavy freezing drizzle
                //1180->Patchy light rain
                //1183->Light rain
                //1186->Moderate rain at times
                1189 -> ModerateRain
                //1192->Heavy rain at times
                1195 -> HeavyRain
                //1198->Light freezing rain
                //1201->Moderate or heavy freezing rain
                //1204->Light sleet
                //1207->Moderate or heavy sleet
                //1210->Patchy light snow
                1213 -> LightSnow
                //1216->Patchy moderate snow
                //1219->Moderate snow
                //1222->Patchy heavy snow
                //1225->Heavy snow
                //1237->Ice pellets
                //1240->Light rain shower
                //1243->Moderate or heavy rain shower
                //1246->Torrential rain shower
                //1249->Light sleet showers
                //1252->Moderate or heavy sleet showers
                //1255->Light snow showers
                //1258->Moderate or heavy snow showers
                //1261->Light showers of ice pellets
                //1264->Moderate or heavy showers of ice pellets
                //1273->Patchy light rain with thunder
                //1276->Moderate or heavy rain with thunder
                //1279->Patchy light snow with thunder
                //1282->Moderate or heavy snow with thunder
                else -> ClearSky
            }
        }
    }
}