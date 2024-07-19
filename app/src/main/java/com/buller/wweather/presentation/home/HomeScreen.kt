package com.buller.wweather.presentation.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.extensions.isNotNull
import com.buller.wweather.R
import com.buller.wweather.domain.model.City
import com.buller.wweather.domain.model.PreferencesState
import kotlinx.coroutines.launch

//@TODO Определять направление ветра: ЮЗ, СВ и тд
@Composable
fun HomeScreen(
    uiState: PageState,
    prefUiState: PreferencesState,
    modifier: Modifier = Modifier,
    onRefreshWeather: () -> Unit,
    openMenu: () -> Unit,
) {
    WeatherFeed(uiState,
        modifier = modifier,
        openMenu = openMenu,
        onRefreshWeather = onRefreshWeather,
        hasWeatherInfo = { hasInfoUiState, contentPadding, contentModifier ->
            Pager(
                hasInfoUiState,
                contentPadding = contentPadding,
                modifier = contentModifier,
                onRefreshWeather = onRefreshWeather,
                prefUiState = prefUiState
            )
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherFeed(
    uiState: PageState,
    onRefreshWeather: () -> Unit,
    openMenu: () -> Unit,
    modifier: Modifier = Modifier,
    hasWeatherInfo: @Composable (
        uiState: PageState.HasInfo, contentPadding: PaddingValues, modifier: Modifier
    ) -> Unit
) {
    val topAppBarState = rememberTopAppBarState()

    Scaffold(
        topBar = {
            HomeTopAppBar(
                openMenu = openMenu,
                topAppBarState = topAppBarState,
                onRefreshWeather = onRefreshWeather,
            )
        }, modifier = modifier
    ) { innerPadding ->
        LoadingContent(empty = when (uiState) {
            is PageState.HasInfo -> false
            is PageState.NoInfo -> uiState.isLoading
        },
            emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading,
            onRefreshWeather = onRefreshWeather,
            content = {
                when (uiState) {
                    is PageState.HasInfo -> {
                        hasWeatherInfo(uiState, innerPadding, modifier)
                    }

                    is PageState.NoInfo -> {
                        TextButton(
                            onClick = onRefreshWeather,
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
fun Pager(
    uiState: PageState.HasInfo,
    prefUiState: PreferencesState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onRefreshWeather: () -> Unit
) {

    val pagerList = uiState.pagerList
    val cities = mutableListOf<City>()

    pagerList.forEach {
        cities.add(it.city)
    }

    val pagerState = rememberPagerState(pageCount = { cities.size }, initialPage = 0)
    var selectedTab by remember { mutableIntStateOf(pagerState.currentPage) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(selectedTab) {
        pagerState.scrollToPage(selectedTab)
    }

    LaunchedEffect(pagerState.currentPage) {
        selectedTab = pagerState.currentPage
    }

    Column(
        modifier = modifier.padding(contentPadding)
    ) {
        CustomIndicatorRowPager(pagerState = pagerState, pageCount = cities.size)

        CustomTabRowWithPartialVisibility(
            pagerState = pagerState, onTabSelected = { index ->
                selectedTab = index
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }, tabs = cities
        )
        HorizontalPager(
            state = pagerState, modifier = Modifier.weight(1f)
        ) { currentPage ->
            WeatherList(
                uiState.pagerList[currentPage],
                prefUiState = prefUiState,
                modifier = modifier,
                onRefreshWeather = onRefreshWeather
            )
        }
    }
}

@Composable
fun WeatherList(
    page: ExamplePage,
    prefUiState: PreferencesState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onRefreshWeather: () -> Unit
) {
    LoadingContent(empty = if (page.weatherInfo.isNotNull()) {
        false
    } else {
        page.isLoading
    },
        emptyContent = { FullScreenLoading() },
        loading = page.isLoading,
        onRefreshWeather = onRefreshWeather,
        content = {
            if (page.weatherInfo.isNotNull()) {
                Column(
                    modifier = modifier
                        .padding(contentPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    WeatherCard(weatherInfo = page.weatherInfo, prefUiState = prefUiState)
                    Spacer(modifier = modifier.padding(8.dp))
                    WeatherInfoList(weatherInfo = page.weatherInfo, prefUiState = prefUiState)
                    Spacer(modifier = modifier.padding(8.dp))
                    Chart(weatherInfo = page.weatherInfo, prefUiState = prefUiState)
                    Spacer(modifier = modifier.padding(32.dp))
                    ForecastList(weatherInfo = page.weatherInfo, prefUiState = prefUiState)
                    Spacer(modifier = modifier.padding(8.dp))
                    AstronomyCard(weatherInfo = page.weatherInfo)
                }
            } else {
                TextButton(
                    onClick = onRefreshWeather, modifier = modifier.fillMaxSize()
                ) {
                    Text(
                        text = stringResource(R.string.home_tap_to_load_content),
                        textAlign = TextAlign.Center
                    )
                }
            }
        })
}


@Composable
fun CustomTabRowWithPartialVisibility(
    pagerState: PagerState, onTabSelected: (Int) -> Unit, tabs: List<City>
) {
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(end = 100.dp)
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(page)
                    }
                    onTabSelected(page)
                }
                .offset {
                    IntOffset(
                        x = -(pagerState.currentPageOffsetFraction * density.density * 100).toInt(),
                        y = 0
                    )
                },
        ) {
            Text(
                text = tabs[page].name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                textAlign = TextAlign.Start,
                color = if (page == pagerState.currentPage) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                fontSize = 36.sp,
                fontWeight = if (page == pagerState.currentPage) {
                    FontWeight.Bold
                } else {
                    FontWeight.Normal
                }
            )
        }
    }
}

@Composable
fun CustomIndicatorRowPager(pagerState: PagerState, pageCount: Int) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp, start = 16.dp, bottom = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.dp, alignment = Alignment.Start
        ),
    ) {
        for (i in 0 until pageCount) {
            val size by animateDpAsState(
                targetValue = if (pagerState.currentPage == i) 16.dp else 8.dp,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
                ),
                label = ""
            )
            val color by animateColorAsState(
                targetValue = if (pagerState.currentPage == i) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                animationSpec = tween(durationMillis = 300),
                label = ""
            )
            Box(
                modifier = Modifier
                    .size(size = size)
                    .clip(CircleShape)
                    .background(color)
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
    onRefreshWeather: () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        PullToRefreshBox(isRefreshing = loading, onRefresh = { onRefreshWeather.invoke() }) {
            content()
        }
    }
}

@Composable
fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    openMenu: () -> Unit,
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? = TopAppBarDefaults.enterAlwaysScrollBehavior(
        topAppBarState
    ),
    onRefreshWeather: () -> Unit,
) {
    TopAppBar(title = {}, actions = {
        AppBarRow(
            openMenu = openMenu,
            modifier = modifier,
            onRefreshWeather = onRefreshWeather
        )
    }, scrollBehavior = scrollBehavior, modifier = modifier
    )
}

@Composable
fun AppBarRow(
    openMenu: () -> Unit,
    modifier: Modifier = Modifier,
    onRefreshWeather: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = { onRefreshWeather.invoke() }) {
            Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = stringResource(R.string.update)
            )
        }
        IconButton(onClick = { openMenu.invoke() }) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(R.string.cd_open_navigation_drawer),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

