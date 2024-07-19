package com.buller.wweather.presentation.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buller.wweather.R
import com.buller.wweather.domain.model.PreferenceDesc
import com.buller.wweather.domain.model.PreferencesState


@Composable
fun SettingsScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier,
    state: PreferencesState,
    onTempCheckedChange: (Boolean) -> Unit,
    onWindCheckedChange: (Boolean) -> Unit,
    onPrecipCheckedChange: (Boolean) -> Unit,
    onPressureCheckedChange: (Boolean) -> Unit,
    onThemeCheckedChange: (Boolean) -> Unit,
    onAutoUpdateCheckedChange: (Boolean) -> Unit,
) {
    SettingsFeed(
        state = state,
        onNavigateUp = onNavigateUp,
        modifier = modifier,
        onAutoUpdateCheckedChange = onAutoUpdateCheckedChange,
        onPrecipCheckedChange = onPrecipCheckedChange,
        onPressureCheckedChange = onPressureCheckedChange,
        onTempCheckedChange = onTempCheckedChange,
        onThemeCheckedChange = onThemeCheckedChange,
        onWindCheckedChange = onWindCheckedChange
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsFeed(
    onNavigateUp: () -> Unit,
    state: PreferencesState,
    onTempCheckedChange: (Boolean) -> Unit,
    onWindCheckedChange: (Boolean) -> Unit,
    onPrecipCheckedChange: (Boolean) -> Unit,
    onPressureCheckedChange: (Boolean) -> Unit,
    onThemeCheckedChange: (Boolean) -> Unit,
    onAutoUpdateCheckedChange: (Boolean) -> Unit,
    modifier: Modifier
) {
    Scaffold(topBar = { SettingsTopAppBar(onNavigateUp = onNavigateUp) })
    { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            Box(modifier = modifier.padding(start = 18.dp)) {
                Column {
                    Text(text = "Your", fontSize = 35.sp)
                    Text(text = "Settings", fontSize = 50.sp)
                }
            }

            Spacer(modifier = modifier.padding(16.dp))

            val temperature = PreferenceDesc.TemperatureItem
            CustomSwitchPreference(
                modifier = modifier,
                title = temperature.title,
                option2 = temperature.option2,
                option1 = temperature.option1,
                description = temperature.description,
                state = state.isCelsius,
                onClick = onTempCheckedChange,
                icon = {}
            )

            val wind = PreferenceDesc.WindItem
            CustomSwitchPreference(
                modifier = modifier,
                title = wind.title,
                option2 = wind.option2,
                option1 = wind.option1,
                description = wind.description,
                state = state.isMetricWindType,
                onClick = onWindCheckedChange,
                icon = {}
            )

            val precip = PreferenceDesc.PrecipItem

            CustomSwitchPreference(
                modifier = modifier,
                title = precip.title,
                option2 = precip.option2,
                option1 = precip.option1,
                description = precip.description,
                state = state.isMetricPrecipType,
                onClick = onPrecipCheckedChange,
                icon = {}
            )

            val pressure = PreferenceDesc.PressureItem
            CustomSwitchPreference(
                modifier = modifier,
                title = pressure.title,
                option2 = pressure.option2,
                option1 = pressure.option1,
                description = pressure.description,
                state = state.isMetricPressureType,
                onClick = onPressureCheckedChange,
                icon = {}
            )
            Spacer(modifier = modifier.padding(16.dp))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.primary,
                thickness = 1.dp,
                modifier = modifier.padding(start = 16.dp, end = 16.dp)
            )
            Spacer(modifier = modifier.padding(8.dp))
            val theme = PreferenceDesc.ThemeItem
            CustomSwitchPreference(
                modifier = modifier,
                title = theme.title,
                option2 = theme.option2,
                option1 = theme.option1,
                description = theme.description,
                state = state.isTheme,
                onClick = onThemeCheckedChange,
                icon = {}
            )
            Spacer(modifier = modifier.padding(8.dp))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.primary,
                thickness = 1.dp,
                modifier = modifier.padding(start = 16.dp, end = 16.dp)
            )
            Spacer(modifier = modifier.padding(16.dp))
            val autoUpdate = PreferenceDesc.AutoUpdateItem
            CustomSwitchPreference(
                modifier = modifier,
                title = autoUpdate.title,
                option2 = autoUpdate.option2,
                option1 = autoUpdate.option1,
                description = autoUpdate.description,
                state = state.isAutoUpdate,
                onClick = onAutoUpdateCheckedChange,
                icon = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopAppBar(
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? = TopAppBarDefaults.enterAlwaysScrollBehavior(
        topAppBarState
    ),
    onNavigateUp: () -> Unit
) {
    TopAppBar(
        title = {},
        actions = {},
        navigationIcon = {
            IconButton(
                onClick = onNavigateUp,
                content = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                    )
                },
            )
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() = Surface {

}