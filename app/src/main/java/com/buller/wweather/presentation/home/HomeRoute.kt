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
    modifier: Modifier = Modifier
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val prefUiState by homeViewModel.prefUiState.collectAsStateWithLifecycle()

    HomeRoute(
        uiState = uiState,
        prefUiState = prefUiState,
        isExpandedScreen = isExpandedScreen,
        openMenu = openMenu,
        onRefreshWeather = {
            homeViewModel.refreshMainWeather()
        },
        modifier = modifier
    )
}


@Composable
fun HomeRoute(
    uiState: PageState,
    prefUiState: PreferencesState,
    isExpandedScreen: Boolean,
    openMenu: () -> Unit,
    onRefreshWeather: () -> Unit,
    modifier: Modifier = Modifier
) {
    val homeScreenType = getHomeScreenType(isExpandedScreen)
    when (homeScreenType) {

        HomeScreenType.Feed -> {
            HomeScreen(
                uiState = uiState,
                prefUiState = prefUiState,
                openMenu = openMenu,
                onRefreshWeather = onRefreshWeather,
                modifier = modifier,
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
