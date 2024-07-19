package com.buller.wweather.presentation.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun SearchRoute(
    viewModel: SearchViewModel,
    isExpandedScreen: Boolean,
    onBack: () -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current
    viewModel.refreshExistCities(context)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreen(
        existCity = uiState,
        isExpandedScreen = isExpandedScreen,
        onBack = onBack,
        onSaveCity = { cityName ->
            viewModel.setCityToLocalDatabase(cityName)
            onBack.invoke()
        },
        onRefreshExistCities = {
            viewModel.refreshExistCities(context)
        },
        modifier = modifier
    )
}