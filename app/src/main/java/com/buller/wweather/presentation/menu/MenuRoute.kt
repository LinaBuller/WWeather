package com.buller.wweather.presentation.menu

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.buller.wweather.presentation.home.HomeViewModel


@Composable
fun MenuRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCities: () -> Unit,
) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val state = homeViewModel.citiesUiState.collectAsStateWithLifecycle().value
    val preferencesState = homeViewModel.prefUiState.collectAsStateWithLifecycle().value

    MenuScreen(
        modifier = modifier,
        uiState = state,
        prefUiState = preferencesState,
        onNavigateToSettings = onNavigateToSettings,
        onNavigateToSearch = onNavigateToSearch,
        onNavigateToCities = onNavigateToCities,
        onItemClick = {

        },
        onRefreshCities = {
            homeViewModel.refreshCities()
        },
        onBack = {
            homeViewModel.refreshCities()
            homeViewModel.refreshMainWeather()
            onBack.invoke()
        }
    )
}