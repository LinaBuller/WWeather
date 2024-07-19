package com.buller.wweather.presentation.search

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buller.wweather.R
import com.buller.wweather.presentation.home.FullScreenLoading

@Composable
fun SearchScreen(
    existCity: SearchUiState,
    modifier: Modifier = Modifier,
    onSaveCity: (String) -> Unit,
    onBack: () -> Unit,
    onRefreshExistCities: () -> Unit,
    isExpandedScreen: Boolean,
) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }

    SearchFeed(
        existCity = existCity,
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
        onRefreshExistCities = onRefreshExistCities,
        hasExistCities = { hasExistCitiesUiState, contentPadding, contentModifier ->
            SearchList(
                existCity = hasExistCitiesUiState,
                modifier = contentModifier,
                contentPadding = contentPadding,
                textState = textState,
                onSaveCity = onSaveCity,
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFeed(
    existCity: SearchUiState,
    showTopAppBar: Boolean,
    onRefreshExistCities: () -> Unit,
    navigationIconContent: @Composable () -> Unit = { },
    modifier: Modifier = Modifier,
    hasExistCities: @Composable (
        state: SearchUiState.HasExistCities,
        contentPadding: PaddingValues,
        modifier: Modifier
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
        },
        modifier = modifier
    ) { innerPadding ->
        LoadingExistCities(
            empty = when (existCity) {
                is SearchUiState.HasExistCities -> false
                is SearchUiState.NoExistCities -> existCity.isLoading
            },
            emptyExistCities = { FullScreenLoading() },
            loading = existCity.isLoading,
            onRefreshExistCities = onRefreshExistCities,
            content = {
                when (existCity) {
                    is SearchUiState.HasExistCities -> hasExistCities(
                        existCity,
                        innerPadding,
                        modifier
                    )

                    is SearchUiState.NoExistCities -> {
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
            }
        )
    }
}

@Composable
fun SearchList(
    existCity: SearchUiState.HasExistCities,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    textState: MutableState<TextFieldValue>,
    onSaveCity: (String) -> Unit
) {
    Box(
        modifier = modifier.padding(contentPadding),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(modifier = modifier.fillMaxWidth()) {
            val context = LocalContext.current
            SearchView(modifier = modifier, state = textState) {
                if (existCity.cities.contains(it)) {
                    onSaveCity.invoke(it)
                } else {
                    Toast.makeText(context, "Not found this city", Toast.LENGTH_LONG).show()
                }
            }
            val filteredOptions = mutableListOf<String>()

            for (city in existCity.cities) {
                if (city.startsWith(prefix = textState.value.text, ignoreCase = true)) {
                    filteredOptions.add(city)
                }
                if (filteredOptions.size == 10) {
                    break
                }
            }
            SearchList(filteredOptions) {
                textState.value = textState.value.copy(text = it)
            }
        }
    }
}

@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    state: MutableState<TextFieldValue>,
    placeHolder: String = "Search here...",
    onClickAdd: (String) -> Unit
) {
    OutlinedTextField(
        value = state.value,
        onValueChange = {
            state.value = it
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        placeholder = {
            Text(text = placeHolder, fontSize = 18.sp)
        },

        maxLines = 1,
        singleLine = true,
        textStyle = TextStyle(fontWeight = FontWeight.Bold),
        trailingIcon = {
            if (state.value.text.isNotBlank()) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Icon: add new city",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = modifier
                        .size(24.dp)
                        .clickable {
                            onClickAdd.invoke(state.value.text)

                        }
                )
            }
        }
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
