package com.buller.wweather.presentation.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buller.wweather.domain.model.PreferenceDesc
import com.buller.wweather.domain.model.PreferencesState


@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    state: PreferencesState,
    onTempCheckedChange: (Boolean) -> Unit,
    onWindCheckedChange: (Boolean) -> Unit,
    onPrecipCheckedChange: (Boolean) -> Unit,
    onPressureCheckedChange: (Boolean) -> Unit,
    onThemeCheckedChange: (Boolean) -> Unit,
    onAutoUpdateCheckedChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    SettingsFeed(
        state = state,
        onBack = onBack,
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
    modifier: Modifier = Modifier,
    state: PreferencesState,
    onTempCheckedChange: (Boolean) -> Unit,
    onWindCheckedChange: (Boolean) -> Unit,
    onPrecipCheckedChange: (Boolean) -> Unit,
    onPressureCheckedChange: (Boolean) -> Unit,
    onThemeCheckedChange: (Boolean) -> Unit,
    onAutoUpdateCheckedChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {

    val allPreferenceItems = listOf(
        PreferenceDesc.TemperatureItem,
        PreferenceDesc.WindItem,
        PreferenceDesc.PressureItem,
        PreferenceDesc.PrecipItem,
        PreferenceDesc.ThemeItem,
        PreferenceDesc.AutoUpdateItem
    )

    Scaffold(
        topBar = {
            SettingsTopAppBar(onBack = onBack)
        })
    { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            item {
                Box(modifier = modifier.padding(start = 36.dp)) {
                    Column {
                        Text(text = "Your", fontSize = 35.sp)
                        Text(text = "Settings", fontSize = 50.sp)
                    }
                }
                Spacer(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                )
            }
            items(allPreferenceItems) { preference ->
                SettingItem(
                    setting = preference,
                    state = state,
                    onClick = { setting, isChecked ->
                        when (setting) {
                            PreferenceDesc.TemperatureItem -> onTempCheckedChange(isChecked)
                            PreferenceDesc.WindItem -> onWindCheckedChange(isChecked)
                            PreferenceDesc.AutoUpdateItem -> onAutoUpdateCheckedChange(isChecked)
                            PreferenceDesc.PrecipItem -> onPrecipCheckedChange(isChecked)
                            PreferenceDesc.PressureItem -> onPressureCheckedChange(isChecked)
                            PreferenceDesc.ThemeItem -> onThemeCheckedChange(isChecked)
                        }
                    })
            }
        }
    }
}

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    setting: PreferenceDesc,
    state: PreferencesState,
    onClick: (PreferenceDesc, Boolean) -> Unit
) {
    val isChecked = when (setting) {
        PreferenceDesc.TemperatureItem -> state.isCelsius
        PreferenceDesc.WindItem -> state.isMetricWindType
        PreferenceDesc.PrecipItem -> state.isMetricPrecipType
        PreferenceDesc.PressureItem -> state.isMetricPressureType
        PreferenceDesc.ThemeItem -> state.isTheme
        PreferenceDesc.AutoUpdateItem -> state.isAutoUpdate
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp)
    ) {
        CustomSwitchPreference(
            modifier = modifier.fillMaxWidth(),
            title = setting.title,
            rightOption = setting.option2,
            leftOption = setting.option1,
            description = setting.description,
            state = isChecked,
            onClick = { checked ->
                onClick(setting, checked)
            },
            icon = {}
        )
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
    onBack: () -> Unit,
) {
    TopAppBar(
        title = {},
        actions = {},
        navigationIcon = {
            IconButton(
                onClick = onBack,
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