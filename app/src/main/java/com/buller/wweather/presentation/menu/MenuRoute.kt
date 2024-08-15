package com.buller.wweather.presentation.menu

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.buller.wweather.presentation.home.HomeViewModel


@Composable
fun MenuRoute(
    viewModel: HomeViewModel,
    isExpandedScreen: Boolean,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCities: () -> Unit,
) {
    val state = viewModel.citiesUiState.collectAsStateWithLifecycle().value
    val prefUiState = viewModel.prefUiState.collectAsStateWithLifecycle().value

    MenuScreen(
        uiState = state,
        modifier = modifier,
        onNavigateToSettings = onNavigateToSettings,
        onNavigateToSearch = onNavigateToSearch,
        onBack = onBack,
        onItemClick = {

        },
        onRefreshCities = {
            viewModel.refreshCities()
        },
        onNavigateToCities = onNavigateToCities,
        prefUiState = prefUiState
    )
}