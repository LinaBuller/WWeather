package com.buller.wweather.presentation.cities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CitiesRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    val citiesViewModel = hiltViewModel<CitiesViewModel>()
    val state by citiesViewModel.uiState.collectAsStateWithLifecycle()

    CitiesScreen(
        modifier = modifier,
        citiesState = state,
        onBack = onBack,
        onDeleteCities = { cities ->
            citiesViewModel.deleteCities(cities)
        },
        onRefreshCities = {
            citiesViewModel.refreshCities()
        },
    )
}