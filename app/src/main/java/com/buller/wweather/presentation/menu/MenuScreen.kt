package com.buller.wweather.presentation.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.buller.wweather.R
import com.buller.wweather.domain.model.City
import com.buller.wweather.presentation.cities.CitiesUiState
import com.buller.wweather.presentation.home.FullScreenLoading


@Composable
fun MenuScreen(
    uiState: CitiesUiState,
    modifier: Modifier = Modifier,
    onItemClick: (City) -> Unit,
    onRefreshCities: () -> Unit,
    onNavigateToCities: () -> Unit,
    onBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSearch: () -> Unit,
) {

    MenuFeed(
        uiState = uiState,
        modifier = modifier,
        onRefreshCities = onRefreshCities,
        onBack = onBack,
        onNavigateToSearch = onNavigateToSearch,
        onNavigateToSettings = onNavigateToSettings,

        hasCities = { hasCity, contentPadding, contentModifier ->
            MenuContent(
                uiState = hasCity,
                contentPadding = contentPadding,
                modifier = contentModifier,
                onItemClick = onItemClick,
                navigateToCities = onNavigateToCities
            )
        })
}

@Composable
fun MenuContent(
    uiState: CitiesUiState.HasCities,
    onItemClick: (City) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    navigateToCities: () -> Unit
) {
    val cities = uiState.cities

    Column(modifier = modifier.padding(contentPadding)) {
        CityList(
            cities = cities,
            onItemClick = onItemClick,
        )
        Spacer(modifier = modifier.height(12.dp))
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Button(
                onClick = {
                    navigateToCities.invoke()
                }, modifier = modifier, enabled = true
            ) {
                Text(text = stringResource(R.string.manage_city))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuFeed(
    uiState: CitiesUiState,
    modifier: Modifier,
    onBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onRefreshCities: () -> Unit,
    hasCities: @Composable (
        uiState: CitiesUiState.HasCities, contentPadding: PaddingValues, modifier: Modifier
    ) -> Unit
) {
    val topAppBarState = rememberTopAppBarState()

    Scaffold(topBar = {
        MenuTopAppBar(
            modifier = modifier,
            topAppBarState = topAppBarState,
            onNavigateToSettings = onNavigateToSettings,
            onNavigateToSearch = onNavigateToSearch,
            onBack = onBack
        )
    }, modifier = modifier) { innerPadding ->
        LoadingContent(
            empty = when (uiState) {
                is CitiesUiState.HasCities -> false
                is CitiesUiState.NoCities -> uiState.isLoading
            }, emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading, onRefreshCities = onRefreshCities, content = {
                when (uiState) {
                    is CitiesUiState.HasCities -> {
                        hasCities(uiState, innerPadding, modifier)
                    }

                    is CitiesUiState.NoCities -> {
                        TextButton(
                            onClick = onRefreshCities,
                            modifier = modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                        ) {
                            Text(
                                text = stringResource(R.string.home_tap_to_load_content),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun CityList(
    cities: List<City>,
    modifier: Modifier = Modifier,
    onItemClick: (City) -> Unit,
) {
    LazyColumn {
        items(cities) { city ->
            CityItem(city = city, modifier = modifier.clickable {
                onItemClick.invoke(city)
            })
        }
    }
}

@Composable
fun CityItem(city: City, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = city.name)
        Box {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = modifier
                        .padding(4.dp)
                        .width(24.dp), painter = painterResource(
                        city.condition?.iconRes ?: R.drawable.sunny_clear_day_icon
                    ), contentDescription = city.condition?.weatherDesc
                )
                Text(text = city.currentTemp.toString())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuTopAppBar(
    onBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSearch: () -> Unit,
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? = TopAppBarDefaults.enterAlwaysScrollBehavior(
        topAppBarState
    ),
) {
    TopAppBar(title = {}, actions = {
        MenuAppBarRow(
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToSettings = onNavigateToSettings
        )
    }, scrollBehavior = scrollBehavior, modifier = modifier, navigationIcon = {
        IconButton(onClick = { onBack.invoke() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = ""
            )
        }
    })
}

@Composable
fun MenuAppBarRow(
    modifier: Modifier = Modifier,
    onNavigateToSettings: () -> Unit,
    onNavigateToSearch: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = { onNavigateToSearch.invoke() }) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = ""
            )
        }
        IconButton(onClick = { onNavigateToSettings.invoke() }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingContent(
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    loading: Boolean,
    onRefreshCities: () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        PullToRefreshBox(isRefreshing = loading, onRefresh = { onRefreshCities.invoke() }) {
            content()
        }
    }
}