package com.buller.wweather.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationResult
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.buller.wweather.R
import com.buller.wweather.presentation.home.HomeRoute
import com.buller.wweather.presentation.search.SearchRoute
import com.buller.wweather.presentation.cities.CitiesRoute
import com.buller.wweather.presentation.cities.CitiesViewModel
import com.buller.wweather.presentation.home.HomeViewModel
import com.buller.wweather.presentation.menu.MenuRoute
import com.buller.wweather.presentation.search.SearchViewModel
import com.buller.wweather.presentation.settings.SettingsRoute
import com.buller.wweather.presentation.settings.SettingsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

@Composable
fun NavGraph(
    isExpandedScreen: Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = NavigationItem.Home.route,
    viewModel: HomeViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.HOME

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composableWithAnimation(route = NavigationItem.Menu.route) {
            MenuRoute(
                viewModel = viewModel,
                isExpandedScreen = isExpandedScreen,
                modifier = modifier,
                onBack = {
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

        composableWithAnimation(route = NavigationItem.Home.route) {
            HomeRoute(
                homeViewModel = viewModel,
                isExpandedScreen = isExpandedScreen,
                openMenu = {
                    navController.navigate(
                        route = NavigationItem.Menu.route,
                    )
                },
                modifier = modifier,
            )
        }
        composableWithAnimation(route = NavigationItem.Search.route) {
            SearchRoute(
                viewModel = hiltViewModel<SearchViewModel>(),
                isExpandedScreen = isExpandedScreen,
                onBack = {
                    navController.popBackStack()
                },
                modifier = modifier
            )
        }

        composableWithAnimation(route = NavigationItem.Settings.route) {
            SettingsRoute(
                viewModel = hiltViewModel<SettingsViewModel>(),
                onBack = {
                    navController.popBackStack()
                },
                modifier = modifier
            )
        }
        composableWithAnimation(route = NavigationItem.Cities.route) {
            CitiesRoute(
                viewModel = hiltViewModel<CitiesViewModel>(),
                isExpandedScreen = isExpandedScreen,
                onBack = {
                    navController.popBackStack()
                },
                onSelectCity = { city ->
                    //@TODO change to City obj
                    viewModel.setWeatherForCity(city.name)
                    navController.popBackStack()
                },
                modifier = modifier
            )
        }
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
fun NavGraphBuilder.composableWithAnimation(
    route: String,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(route = route) {
        val offsetX = remember { Animatable(0f) }
        val coroutineScope = rememberCoroutineScope()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationX = offsetX.value
                }
        ) {
            content(it)
        }

        LaunchedEffect(it) {
            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 300)
            )
        }

        DisposableEffect(it) {
            onDispose {
                coroutineScope.launch {
                    offsetX.animateTo(
                        targetValue =
                        if (it.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                            -1000f
                        else 1000f,
                        animationSpec = tween(durationMillis = 300)
                    )
                    

//                    suspendCancellableCoroutine { continuation ->
//                        launch {
//
//                            offsetX.snapTo(offsetX.targetValue)
//                        }
//                        continuation.resume(Unit) {}
//                    }
                }
            }
        }
    }
}