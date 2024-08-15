package com.buller.wweather.presentation.cities

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buller.wweather.R
import com.buller.wweather.domain.model.City
import com.buller.wweather.presentation.home.FullScreenLoading
import kotlinx.coroutines.launch


@Composable
fun CitiesScreen(
    cities: CitiesUiState,
    isExpandedScreen: Boolean,
    onBack: () -> Unit,
    onRefreshCities: () -> Unit,
    onDeleteCities: (List<City>) -> Unit,
    modifier: Modifier
) {

    var isActionModeEnabled by remember { mutableStateOf(false) }
    val selectedItems = remember { mutableStateListOf<City>() }

    CitiesFeed(cities = cities,
        modifier = modifier,
        navigationIconContent = {
            IconButton(onClick = {
                if (isActionModeEnabled) {
                    isActionModeEnabled = false
                    selectedItems.clear()
                } else {
                    onBack()
                }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.cd_navigate_up),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

        },
        isActionModeEnabled = isActionModeEnabled,
        showTopAppBar = !isExpandedScreen,
        onRefreshCities = onRefreshCities,
        onCloseActionMode = { isEnabled ->
            isActionModeEnabled = isEnabled
            if (isEnabled) {
                selectedItems.clear()
            }
        },
        selectedItems = selectedItems,
        onDeleteCities = {
            onDeleteCities(selectedItems)
        },
        hasExistCities = { hasCitiesUiState, contentPadding, contentModifier, lazyListState ->
            CitiesList(
                contentPadding = contentPadding,
                cities = hasCitiesUiState,
                modifier = contentModifier,
                lazyListState = lazyListState,
                isActionModeEnabled = isActionModeEnabled,
                selectedItems = selectedItems,
                onCloseActionMode = {
                    isActionModeEnabled = it
                }
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
    isActionModeEnabled: Boolean,
    onCloseActionMode: (Boolean) -> Unit,
    selectedItems: SnapshotStateList<City>,
    onDeleteCities: () -> Unit,
    hasExistCities: @Composable (
        state: CitiesUiState.HasCities,
        contentPadding: PaddingValues,
        modifier: Modifier,
        lazyListState: LazyListState
    ) -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    val lazyListState = rememberLazyListState()

    Scaffold(topBar = {
        if (showTopAppBar) {
            CitiesTopAppBar(
                navigationIconContent = navigationIconContent,
                topAppBarState = topAppBarState,
                isActionModeEnabled = isActionModeEnabled,
                selectedItems = selectedItems,
                onDeleteSelected = onDeleteCities,
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
                    is CitiesUiState.HasCities -> hasExistCities(
                        cities,
                        innerPadding,
                        modifier, lazyListState
                    )

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CitiesList(
    cities: CitiesUiState.HasCities,
    contentPadding: PaddingValues,
    modifier: Modifier,
    lazyListState: LazyListState,
    isActionModeEnabled: Boolean,
    selectedItems: SnapshotStateList<City>,
    onCloseActionMode: (Boolean) -> Unit
) {
    LazyColumn(modifier = modifier.padding(contentPadding), state = lazyListState) {
        items(cities.cities) { city ->
            val isSelected = selectedItems.contains(city)
            CitiesItem(
                city = city,
                isSelected = isSelected,
                modifier = modifier.combinedClickable(
                    onClick = {
                        if (isActionModeEnabled) {
                            if (isSelected) {
                                selectedItems.remove(city)
                            } else {
                                selectedItems.add(city)
                            }
                            onCloseActionMode(selectedItems.isNotEmpty())
                        }
                    },
                    onLongClick = {
                        if (!isSelected) {
                            selectedItems.add(city)
                        }
                        onCloseActionMode(true)
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
            )
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
    ),
    isActionModeEnabled: Boolean,
    selectedItems: SnapshotStateList<City>,
    onDeleteSelected: () -> Unit
) {
    if (isActionModeEnabled) {
        CenterAlignedTopAppBar(
            navigationIcon = navigationIconContent,
            scrollBehavior = scrollBehavior,
            modifier = modifier,
            title = {
                Text("Select ${selectedItems.size}")
            },
            actions = {
                IconButton(onClick = onDeleteSelected) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            })
    } else {
        TopAppBar(
            title = {},
            navigationIcon = navigationIconContent,
            scrollBehavior = scrollBehavior,
            modifier = modifier
        )
    }
}


@Composable
fun CitiesItem(city: City, isSelected: Boolean, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .border(
                shape = RoundedCornerShape(15.dp),
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
            ),
        shape = RoundedCornerShape(15.dp),
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text(text = city.country, fontSize = 12.sp)
                Text(text = city.name, fontSize = 36.sp, modifier = modifier.padding(start = 16.dp))
                Text(text = city.region, fontSize = 12.sp)
            }
            Column(
                modifier = modifier
                    .fillMaxHeight()
                    .padding(16.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(text = "12:12")
                Text(text = "1th January 2025")
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun CitiesScreenPreview() {
    val city1 = City(id = 1, country = "Russia", region = "Omskaya oblast", name = "Omsk")
    val city2 = City(id = 2, country = "Russia", region = "Omskaya oblast", name = "Ichsim")
    val city3 = City(id = 3, country = "Russia", region = "Omskaya oblast", name = "Tara")
    val cities = listOf(city1, city2, city3)

    val cityState =
        CitiesUiState.HasCities(cities = cities, isLoading = false, errorMessages = null)


    CitiesScreen(modifier = Modifier,
        cities = cityState,
        isExpandedScreen = false,
        onRefreshCities = {},
        onDeleteCities = {},
        onBack = {})
}

