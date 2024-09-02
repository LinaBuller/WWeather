package com.buller.wweather.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.buller.wweather.presentation.home.HomeRoute
import com.buller.wweather.presentation.search.SearchRoute
import com.buller.wweather.presentation.cities.CitiesRoute
import com.buller.wweather.presentation.cities.CitiesViewModel
import com.buller.wweather.presentation.home.HomeViewModel
import com.buller.wweather.presentation.menu.MenuRoute
import com.buller.wweather.presentation.search.SearchViewModel
import com.buller.wweather.presentation.settings.SettingsRoute
import com.buller.wweather.presentation.settings.SettingsViewModel

@Composable
fun NavGraph(
    startDestination: String = NavigationItem.Home.route,
) {
    val navController = rememberNavController()
    val homeViewModel = hiltViewModel<HomeViewModel>()

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(route = NavigationItem.Home.route) {
            HomeRoute(homeViewModel = homeViewModel,
                openMenu = {
                    navController.navigate(
                        route = NavigationItem.Menu.route
                    )
                }
            )
        }

        composable(route = NavigationItem.Menu.route) {
            MenuRoute(
                onNavigateToSettings = {
                    navController.navigate(
                        route = NavigationItem.Settings.route
                    )
                },
                onNavigateToCities = {
                    navController.navigate(
                        route = NavigationItem.Cities.route
                    )
                },
                onNavigateToSearch = {
                    navController.navigate(
                        route = NavigationItem.Search.route
                    )
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = NavigationItem.Search.route) {
            SearchRoute(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = NavigationItem.Settings.route) {
            SettingsRoute(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = NavigationItem.Cities.route) {
            CitiesRoute(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}