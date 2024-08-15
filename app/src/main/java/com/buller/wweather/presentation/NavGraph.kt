package com.buller.wweather.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
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
    isExpandedScreen: Boolean,
    startDestination: String = NavigationItem.Home.route,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<HomeViewModel>()
    val lifecycleOwner = LocalLifecycleOwner.current

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(route = NavigationItem.Home.route) {
            HomeRoute(
                homeViewModel = viewModel,
                isExpandedScreen = isExpandedScreen,
                openMenu = {
                    navController.navigate(
                        route = NavigationItem.Menu.route
                    )
                },
                modifier = modifier
            )
        }
        composable(route = NavigationItem.Menu.route) {
            MenuRoute(
                viewModel = viewModel,
                isExpandedScreen = isExpandedScreen,
                onBack = {
                    viewModel.refreshCities()
                    viewModel.refreshMainWeather()
                    navController.popBackStack()
                },
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
                })
        }
        composable(route = NavigationItem.Search.route) {
            SearchRoute(
                viewModel = hiltViewModel<SearchViewModel>(),
                isExpandedScreen = isExpandedScreen,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = NavigationItem.Settings.route) {
            SettingsRoute(
                viewModel = hiltViewModel<SettingsViewModel>(),
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = NavigationItem.Cities.route) {
            CitiesRoute(
                viewModel = hiltViewModel<CitiesViewModel>(),
                isExpandedScreen = isExpandedScreen,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}