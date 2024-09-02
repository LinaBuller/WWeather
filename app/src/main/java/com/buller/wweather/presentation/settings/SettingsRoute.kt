package com.buller.wweather.presentation.settings


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.buller.wweather.domain.model.PreferencesState


@Composable
fun SettingsRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val state by settingsViewModel.prefUiState.collectAsStateWithLifecycle()

    SettingsScreen(
        modifier = modifier.fillMaxWidth(),
        state = state,
        onBack = onBack,
        onWindCheckedChange = { isType ->
            settingsViewModel.updateWindType(isType)
        },
        onTempCheckedChange = { isType ->
            settingsViewModel.updateTempType(isType)
        },
        onThemeCheckedChange = { isDark ->
            settingsViewModel.updatePreferTheme(isDark)
        },
        onPressureCheckedChange = { isType ->
            settingsViewModel.updatePressureType(isType)
        },
        onPrecipCheckedChange = { isType ->
            settingsViewModel.updatePrecipType(isType)
        },
        onAutoUpdateCheckedChange = { isAutoUpdate ->
            settingsViewModel.updateIsAutoUpdate(isAutoUpdate)
        })
}
