package com.buller.wweather.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.buller.wweather.domain.model.PreferencesState


@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel,
    isExpandedScreen: Boolean,
    openMenu: () -> Unit,
    modifier: Modifier,
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val prefUiState by homeViewModel.prefUiState.collectAsStateWithLifecycle()

    HomeRoute(
        uiState = uiState,
        prefUiState = prefUiState,
        isExpandedScreen = isExpandedScreen,
        openMenu = openMenu,
        modifier = modifier,
        onRefreshWeather = {
            homeViewModel.refreshMainWeather()
        },
    )
}


@Composable
fun HomeRoute(
    uiState: PageState,
    prefUiState: PreferencesState,
    isExpandedScreen: Boolean,
    modifier: Modifier,
    openMenu: () -> Unit,
    onRefreshWeather: () -> Unit,
) {
    val homeScreenType = getHomeScreenType(isExpandedScreen)

    when (homeScreenType) {

        HomeScreenType.Feed -> {
            HomeScreen(
                uiState = uiState,
                prefUiState = prefUiState,
                openMenu = openMenu,
                modifier = modifier,
                onRefreshWeather = {
                    onRefreshWeather.invoke()
                },
            )
        }

        HomeScreenType.FeedWithCharts -> {
            //@TODO Add expanded screen
        }
    }
}

private enum class HomeScreenType {
    Feed,
    FeedWithCharts
}

@Composable
private fun getHomeScreenType(
    isExpandedScreen: Boolean,
): HomeScreenType = when (isExpandedScreen) {
    false -> HomeScreenType.Feed
    true -> HomeScreenType.FeedWithCharts
}
