package com.buller.wweather.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.buller.wweather.domain.model.PageState
import com.buller.wweather.domain.model.PreferencesState


@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel,
    openMenu: () -> Unit
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val prefUiState by homeViewModel.prefUiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        prefUiState = prefUiState,
        openMenu = openMenu,
        onRefreshWeather = {
            homeViewModel.refreshMainWeather()
        }
    )
}




