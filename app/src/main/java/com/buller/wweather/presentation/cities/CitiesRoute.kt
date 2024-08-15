package com.buller.wweather.presentation.cities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.buller.wweather.domain.model.City

@Composable
fun CitiesRoute(
    viewModel: CitiesViewModel,
    isExpandedScreen: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CitiesScreen(
        cities = uiState,
        isExpandedScreen = isExpandedScreen,
        onBack = onBack,
        onDeleteCities = { viewModel.deleteCities(it) },
        onRefreshCities = {
            viewModel.refreshCities()
        },
        modifier = modifier
    )
}