package com.buller.wweather.presentation.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.buller.wweather.presentation.settings.components.SettingsSwitch

@Composable
fun CustomSwitchPreference(
    modifier: Modifier = Modifier,
    title: String,
    option1: String,
    option2: String,
    description: String? = null,
    state: Boolean,
    onClick: (Boolean) -> Unit,
    icon: @Composable (() -> Unit)? = null
) {
    CustomSwitchPreference(
        title = title,
        modifier = modifier,
        icon = icon,
        description = description,
        option1 = option1,
        option2 = option2,
        checked = state,
        onCheckedChange = onClick)
}

@Composable
fun CustomSwitchPreference(
    title: String,
    description: String? = null,
    icon: @Composable (() -> Unit)?,
    checked: Boolean,
    option1: String,
    option2: String,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    PreferenceItem(
        checked = checked,
        title = title,
        description = description,
        modifier = modifier,
        icon = icon,
        onCheckedChange = onCheckedChange,
        onClick = {}
    ) {
        SettingsSwitch(
            checked = checked,
            option1 = option1,
            option2 = option2,
            onOptionSelected = onCheckedChange
        )
    }
}
