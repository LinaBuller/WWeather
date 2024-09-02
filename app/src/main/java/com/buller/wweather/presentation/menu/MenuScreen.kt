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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buller.wweather.R
import com.buller.wweather.domain.model.City
import com.buller.wweather.domain.model.PreferencesState
import com.buller.wweather.domain.model.WeatherType
import com.buller.wweather.domain.model.CitiesUiState
import com.buller.wweather.presentation.home.FullScreenLoading

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    uiState: CitiesUiState,
    prefUiState: PreferencesState,
    onItemClick: (City) -> Unit,
    onRefreshCities: () -> Unit,
    onNavigateToCities: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onBack: () -> Unit,
) {

    MenuFeed(
        modifier = modifier,
        uiState = uiState,
        onRefreshCities = onRefreshCities,
        onBack = onBack,
        onNavigateToSearch = onNavigateToSearch,
        onNavigateToSettings = onNavigateToSettings,

        menuContent = { hasCity, contentPadding, contentModifier ->
            MenuContent(
                uiState = hasCity,
                contentPadding = contentPadding,
                modifier = contentModifier,
                onItemClick = onItemClick,
                navigateToCities = onNavigateToCities,
                prefUiState = prefUiState
            )
        })
}

@Composable
fun MenuContent(
    modifier: Modifier = Modifier,
    uiState: CitiesUiState.HasCities,
    prefUiState: PreferencesState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onItemClick: (City) -> Unit,
    navigateToCities: () -> Unit
) {
    val cities = uiState.cities

    Column(modifier = modifier
        .padding(contentPadding)
        .fillMaxSize()) {
        CityList(
            cities = cities,
            onItemClick = onItemClick,
            prefUiState = prefUiState,
            navigateToCities = navigateToCities,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuFeed(
    modifier: Modifier,
    uiState: CitiesUiState,
    onBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onRefreshCities: () -> Unit,
    menuContent: @Composable (
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
            loading = uiState.isLoading,
            onRefreshCities = onRefreshCities,
            content = {
                when (uiState) {
                    is CitiesUiState.HasCities -> {
                        menuContent(uiState, innerPadding, modifier)
                    }

                    is CitiesUiState.NoCities -> {
                        TextButton(
                            onClick = onRefreshCities,
                            modifier = modifier.padding(innerPadding)
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
    modifier: Modifier = Modifier,
    cities: List<City>,
    prefUiState: PreferencesState,
    onItemClick: (City) -> Unit,
    navigateToCities: () -> Unit
) {
    LazyColumn {
        items(cities) { city ->
            CityItem(
                city = city,
                modifier = modifier.clickable {
                    onItemClick.invoke(city)
                },
                prefUiState = prefUiState
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.primary,
                thickness = 1.dp,
                modifier = modifier.padding(start = 16.dp, end = 16.dp)
            )
        }
        item {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)) {
                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Button(
                        onClick = {
                            navigateToCities.invoke()
                        },
                        modifier = modifier,
                        enabled = true,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(
                            text = stringResource(R.string.manage_city),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = modifier.height(48.dp))
            }
        }
    }
}

@Composable
fun CityItem(
    modifier: Modifier = Modifier,
    city: City,
    prefUiState: PreferencesState
) {
    val temp: String
    val tempSign: String
    if (prefUiState.isCelsius) {
        temp = city.currentTempC.toString()
        tempSign = "°C"
    } else {
        temp = city.currentTempF.toString()
        tempSign = "°F"
    }
//    Row(
//        modifier = modifier
//            .padding(16.dp)
//            .fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        val timestamp = city.currentTimestamp
//        val timeZoneId = city.timeZoneId
//        val currentTime =
//            TimeFormatter.getShort24HourFormattedTime(timestamp, timeZoneId ?: "Etc/UTC")
//
//        Text(text = currentTime ?: "--:--", style = MaterialTheme.typography.bodyLarge)
//        Column(
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(text = buildAnnotatedString {
//                withStyle(
//                    style = SpanStyle(
//                        fontSize = 25.sp,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                ) {
//                    append(city.name.first().toString())
//                }
//                withStyle(style = SpanStyle(fontSize = 18.sp)) {
//                    append(city.name.substring(1))
//                }
//            })
//            Icon(
//                modifier = modifier.size(72.dp), painter = painterResource(
//                    city.condition?.iconRes ?: R.drawable.fog_icon
//                ), contentDescription = city.condition?.weatherDesc
//            )
//        }
//        Text(text = "$temp$tempSign", style = MaterialTheme.typography.bodyLarge)
//    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(text = city.country, fontSize = 12.sp)
        Text(text = city.name, fontSize = 26.sp, modifier = modifier.padding(start = 16.dp))
        Text(text = city.region, fontSize = 12.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuTopAppBar(
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? = TopAppBarDefaults.enterAlwaysScrollBehavior(
        topAppBarState
    ),
    onBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSearch: () -> Unit,
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
    loading: Boolean,
    onRefreshCities: () -> Unit,
    emptyContent: @Composable () -> Unit,
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

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    val city1 = City(
        id = 1,
        name = "Porto",
        isPin = false,
        country = "Portugal",
        region = "Porto",
        position = 0
    )
    city1.currentTempC = 35.toString()
    city1.currentTempF = 35.toString()
    city1.condition = WeatherType.fromWNO(1003)


    val city2 = City(
        id = 2,
        name = "Moskow",
        isPin = false,
        country = "Russia",
        region = "Moskovskaya oblast",
        position = 0
    )
    city2.currentTempC = 21.toString()
    city2.currentTempF = 21.toString()
    city2.condition = WeatherType.fromWNO(1000)


    val city3 = City(
        id = 3,
        name = "Omsk",
        isPin = false,
        country = "Russia",
        region = "Omskaya oblast",
        position = 0
    )
    city3.currentTempC = 23.toString()
    city3.currentTempF = 35.toString()
    city3.condition = WeatherType.fromWNO(1087)


    val sampleUiState = CitiesUiState.HasCities(
        isLoading = false,
        cities = listOf(city1, city2, city3),
        errorMessages = null
    )
    val preUiState = PreferencesState(isCelsius = false)
    Surface {

        MenuScreen(
            uiState = sampleUiState,
            onNavigateToCities = { },
            onRefreshCities = {},
            onBack = {},
            onNavigateToSearch = {},
            onNavigateToSettings = { },
            onItemClick = {},
            prefUiState = preUiState,
        )
    }
}