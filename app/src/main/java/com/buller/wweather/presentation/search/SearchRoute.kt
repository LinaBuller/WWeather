package com.buller.wweather.presentation.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun SearchRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    val searchViewModel  = hiltViewModel<SearchViewModel>()
    val state by searchViewModel.searchUiState.collectAsStateWithLifecycle()

    SearchScreen(
        modifier = modifier,
        locationState = state,
        onBack = onBack,
        onSaveCity = { city ->
            searchViewModel.setCityToLocalDatabase(city)
            onBack.invoke()
        },
        onRefreshSearchRequest = {
            searchViewModel.refreshSearchRequest()
        },
        onSearchTextChanged = { text ->
            searchViewModel.emitSearchText(text)
        }
    )
}