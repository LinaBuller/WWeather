package com.buller.wweather.presentation

enum class Screen(val route: String) {
    HOME("home"),
    SEARCH("search"),
    SETTINGS("settings"),
    CITIES("cities"),
    MENU("menu")
}

sealed class NavigationItem(val route: String) {
    data object Home : NavigationItem(Screen.HOME.name)
    data object Search : NavigationItem(Screen.SEARCH.name)
    data object Settings : NavigationItem(Screen.SETTINGS.name)
    data object Cities : NavigationItem(Screen.CITIES.name)
    data object Menu : NavigationItem(Screen.MENU.name)
}