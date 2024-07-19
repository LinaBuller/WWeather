package com.buller.wweather.domain.model


data class PreferencesState(
    var isCelsius: Boolean = false,
    var isMetricWindType: Boolean = false,
    var isMetricPrecipType: Boolean = false,
    var isMetricPressureType: Boolean = false,
    var isTheme: Boolean= false,
    var isAutoUpdate: Boolean = false
)


sealed class PreferenceDesc(
    val title: String,
    val option1: String,
    val option2: String,
    val description: String,
) {
    data object TemperatureItem : PreferenceDesc(
        title = "Temperature",
        option1 = "F",
        option2 = "C",
        //TODO
        description = "Pick your favorite way to measure temperature"
    )

    data object WindItem : PreferenceDesc(
        title = "Wind",
        option1 = "mph",
        option2 = "m/s",
        description = "Select your preferred unit for wind speed"
    )

    data object PrecipItem : PreferenceDesc(
        title = "Precipitation",
        option1 = "in",
        option2 = "mm",
        description = "Set your preferred unit for measuring rainfall"
    )

    data object PressureItem :
        PreferenceDesc(
            title = "Pressure",
            option1 = "in",
            option2 = "mb",
            description = "Select your preferred unit for atmospheric pressure")

    data object ThemeItem :
        PreferenceDesc(
            title = "Theme",
            option1 = "Light",
            option2 = "Dark",
            description = "Toggle between light and dark themes")

    data object AutoUpdateItem :
        PreferenceDesc(
            title = "Auto update",
            option1 = "No",
            option2 = "Yes",
            description = "Manage automatic updates for the app")

}