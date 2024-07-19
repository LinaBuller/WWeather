package com.buller.wweather.presentation.cities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.buller.wweather.R
import com.buller.wweather.domain.model.City
import com.buller.wweather.presentation.home.FullScreenLoading


@Composable
fun CitiesScreen(
    cities: CitiesUiState,
    isExpandedScreen: Boolean,
    onBack: () -> Unit,
    onSelectCity: (City) -> Unit,
    onRefreshCities: () -> Unit,
    modifier: Modifier
) {
    CitiesFeed(cities = cities,
        modifier = modifier,
        navigationIconContent = {
            if (!isExpandedScreen) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.cd_navigate_up),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        showTopAppBar = !isExpandedScreen,
        onRefreshCities = onRefreshCities,
        hasExistCities = { hasCitiesUiState, contentPadding, contentModifier ->
            CitiesList(
                hasCitiesUiState,
                contentPadding = contentPadding,
                modifier = contentModifier,
                onSelectCity = onSelectCity
            )
        })


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesFeed(
    cities: CitiesUiState,
    showTopAppBar: Boolean,
    onRefreshCities: () -> Unit,
    navigationIconContent: @Composable () -> Unit = { },
    modifier: Modifier = Modifier,
    hasExistCities: @Composable (
        state: CitiesUiState.HasCities, contentPadding: PaddingValues, modifier: Modifier
    ) -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    Scaffold(topBar = {
        if (showTopAppBar) {
            CitiesTopAppBar(
                navigationIconContent = navigationIconContent,
                topAppBarState = topAppBarState,
                modifier = modifier
            )
        }
    }, modifier = modifier) { innerPadding ->
        LoadingCities(empty = when (cities) {
            is CitiesUiState.HasCities -> false
            is CitiesUiState.NoCities -> cities.isLoading
        },
            emptyCities = { FullScreenLoading() },
            loading = cities.isLoading,
            onRefreshCities = onRefreshCities,
            content = {
                when (cities) {
                    is CitiesUiState.HasCities -> hasExistCities(cities, innerPadding, modifier)
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
            })
    }
}

@Composable
fun CitiesList(
    cities: CitiesUiState.HasCities,
    contentPadding: PaddingValues,
    onSelectCity: (City) -> Unit,
    modifier: Modifier,
) {
    LazyColumn(modifier = modifier.padding(contentPadding)) {
        items(cities.cities) { city ->
            CitiesItem(city = city, modifier = modifier.clickable {
                onSelectCity.invoke(city)
            })
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingCities(
    empty: Boolean,
    emptyCities: @Composable () -> Unit,
    loading: Boolean,
    onRefreshCities: () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyCities()
    } else {
        PullToRefreshBox(isRefreshing = loading, onRefresh = { onRefreshCities.invoke() }) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesTopAppBar(
    navigationIconContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? = TopAppBarDefaults.enterAlwaysScrollBehavior(
        topAppBarState
    )
) {
    TopAppBar(
        title = {},
        navigationIcon = navigationIconContent,
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}


@Composable
fun CitiesItem(city: City, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {

            },
        shape = RoundedCornerShape(15.dp),
    ) {
        Row(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Column {
                    Text(text = city.name)
                    city.condition?.weatherDesc?.let { Text(text = it) }
                }
            }
            Box {
                Column {
                    city.currentTemp?.let { Text(text = it) }
                    Text(text = "max 21 / min 12")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCitiesItem() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

            },
        shape = RoundedCornerShape(15.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Column {
                    Text(text = "City")
                    Text(text = "Country")
                }
            }
            Box {
                Column {
                    Text(text = "21")
                    Text(text = "max 21 / min 12")
                }
            }
        }
    }
}