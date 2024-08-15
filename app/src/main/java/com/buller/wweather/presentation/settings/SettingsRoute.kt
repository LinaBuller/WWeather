package com.buller.wweather.presentation.settings


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier


@Composable
fun SettingsRoute(viewModel: SettingsViewModel, onBack: () -> Unit, modifier: Modifier = Modifier) {

    val uiState by viewModel.prefUiState.collectAsState()

    SettingsScreen(
        state = uiState,
        onNavigateUp = onBack,
        modifier = modifier.fillMaxWidth(),
        onWindCheckedChange = { isType ->
            viewModel.updateWindType(isType)
        },
        onTempCheckedChange = { isType ->
            viewModel.updateTempType(isType)
        },
        onThemeCheckedChange = { isDark ->
            viewModel.updatePreferTheme(isDark)
        },
        onPressureCheckedChange = { isType ->
            viewModel.updatePressureType(isType)
        },
        onPrecipCheckedChange = { isType ->
            viewModel.updatePrecipType(isType)
        },
        onAutoUpdateCheckedChange = { isAutoUpdate ->
            viewModel.updateIsAutoUpdate(isAutoUpdate)
        })
}
