package com.buller.wweather.presentation.search

import androidx.activity.compose.BackHandler
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buller.wweather.R
import com.buller.wweather.domain.model.LocationInfo
import com.buller.wweather.presentation.home.FullScreenLoading

@Composable
fun SearchScreen(
    locationState: LocationState,
    modifier: Modifier = Modifier,
    onSaveCity: (LocationInfo) -> Unit,
    onBack: () -> Unit,
    onRefreshSearchRequest: () -> Unit,
    isExpandedScreen: Boolean,
    onSearchTextChanged: (String) -> Unit
) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    SearchFeed(locationState = locationState,
        modifier = modifier,
        navigationIconContent = {
            if (!isExpandedScreen) {
                IconButton(onClick = {
                    focusRequester.requestFocus()
                    keyboardController?.hide()
                    onBack.invoke()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.cd_navigate_up),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        showTopAppBar = !isExpandedScreen,
        onRefreshExistCities = onRefreshSearchRequest,
        hasExistCities = { hasInfoUiState, contentPadding, contentModifier ->
            SearchList(
                locationState = hasInfoUiState,
                modifier = contentModifier,
                contentPadding = contentPadding,
                textState = textState,
                focusRequester = focusRequester,
                onSaveCity = onSaveCity,
                onSearchTextChanged = onSearchTextChanged
            )
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFeed(
    locationState: LocationState,
    showTopAppBar: Boolean,
    onRefreshExistCities: () -> Unit,
    navigationIconContent: @Composable () -> Unit = { },
    modifier: Modifier = Modifier,
    hasExistCities: @Composable (
        state: LocationState.SearchHasInfo, contentPadding: PaddingValues, modifier: Modifier
    ) -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    Scaffold(
        topBar = {
            if (showTopAppBar) {
                SearchTopAppBar(
                    navigationIconContent = navigationIconContent,
                    modifier = modifier,
                    topAppBarState = topAppBarState,
                )
            }
        }, modifier = modifier
    ) { innerPadding ->
        LoadingExistCities(empty = when (locationState) {
            is LocationState.SearchHasInfo -> false
            is LocationState.SearchNoInfo -> locationState.isLoading
        },
            emptyExistCities = {},
            loading = locationState.isLoading,
            onRefreshExistCities = onRefreshExistCities,
            content = {
                when (locationState) {
                    is LocationState.SearchHasInfo -> hasExistCities(
                        locationState, innerPadding, modifier
                    )

                    is LocationState.SearchNoInfo -> {
                        TextButton(
                            onClick = onRefreshExistCities,
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
fun SearchList(
    locationState: LocationState.SearchHasInfo,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    textState: MutableState<TextFieldValue>,
    focusRequester: FocusRequester,
    onSaveCity: (LocationInfo) -> Unit,
    onSearchTextChanged: (String) -> Unit,
) {
    Column(modifier = modifier.padding(contentPadding)) {
        SearchView(
            state = textState,
            focusRequester = focusRequester,
            onSearchTextChanged = onSearchTextChanged
        )
        SearchList(locations = locationState.locationList, onItemClick = onSaveCity)
    }
}

@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    state: MutableState<TextFieldValue>,
    placeHolder: String = "Search here...",
    focusRequester: FocusRequester,
    onSearchTextChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = state.value,
        onValueChange = {
            state.value = it
            onSearchTextChanged(it.text)
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .focusRequester(focusRequester = focusRequester),
        placeholder = {
            Text(text = placeHolder, fontSize = 18.sp)
        },

        maxLines = 1,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingExistCities(
    empty: Boolean,
    emptyExistCities: @Composable () -> Unit,
    loading: Boolean,
    onRefreshExistCities: () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyExistCities()
    } else {
        PullToRefreshBox(
            isRefreshing = loading,
            onRefresh = { onRefreshExistCities.invoke() })
        {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBar(
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
fun SearchList(
    locations: List<LocationInfo>,
    modifier: Modifier = Modifier,
    onItemClick: (LocationInfo) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(locations) {
            SearchItem(location = it, modifier = modifier, onItemClick)
        }
    }
}

@Composable
fun SearchItem(
    location: LocationInfo, modifier: Modifier = Modifier, onItemClick: (LocationInfo) -> Unit
) {
    Column(modifier = modifier
        .clickable {
            onItemClick(location)
        }
        .padding(16.dp)
        .fillMaxWidth()) {


        Text(text = location.country, fontSize = 12.sp)
        Text(text = location.name, fontSize = 36.sp, modifier = modifier.padding(start = 16.dp))
        Text(text = location.region, fontSize = 12.sp)

        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary,
            thickness = 1.dp,
            modifier = modifier.padding(top = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {

    val locationList = listOf(
        LocationInfo(
            id = 125, name = "London", country = "Great Britain", region = "Island"
        ),
        LocationInfo(
            id = 124, name = "Moscow", country = "Russia", region = "Eurasia"
        ),
    )
    val location = LocationState.SearchHasInfo(
        locationList = locationList, isLoading = false, errorMessages = null
    )

    SearchScreen(locationState = location,
        onSearchTextChanged = {},
        modifier = Modifier,
        isExpandedScreen = false,
        onBack = {},
        onSaveCity = {},
        onRefreshSearchRequest = {})
}