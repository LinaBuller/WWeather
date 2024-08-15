package com.buller.wweather.presentation.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun SearchRoute(
    viewModel: SearchViewModel,
    isExpandedScreen: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val searchUi by viewModel.searchUiState.collectAsStateWithLifecycle()
    SearchScreen(

        locationState = searchUi,
        isExpandedScreen = isExpandedScreen,
        onBack = onBack,
        onSaveCity = { city ->
            viewModel.setCityToLocalDatabase(city)
            onBack.invoke()
        },
        onRefreshSearchRequest = {
            viewModel.refreshSearchRequest()
        },
        onSearchTextChanged = { text ->
            viewModel.emitSearchText(text)
        },
        modifier = modifier,
    )
}