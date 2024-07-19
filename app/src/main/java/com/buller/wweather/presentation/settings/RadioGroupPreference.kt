package com.buller.wweather.presentation.settings

import android.util.Log
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.buller.wweather.presentation.settings.components.Choice
import com.buller.wweather.presentation.settings.components.RadioGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

@Composable
fun <V> RadioGroupPreference(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    preferenceKey: Preferences.Key<Boolean>,
    dataStore: DataStore<Preferences>,
    choices: List<Choice<out V>>,
    icon: @Composable (() -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    val currentValueFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[preferenceKey] ?: false
    }
    var currentValue by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = preferenceKey) {
        currentValueFlow.catch { exception ->
            Log.e("SwitchPreference", "Error reading preference", exception)
        }.collect { value ->
            currentValue = value
        }
    }

    RadioGroupPreference(
        icon = icon,
        description = description,
        modifier = modifier,
        title = title,
        choices = choices,
        onSelect = { newValue ->
            currentValue = newValue is Boolean && newValue
            scope.launch {
                dataStore.edit { preferences ->
                    preferences[preferenceKey] = currentValue
                }
            }
        },
        selected = currentValue.toString(),
    )
}

@Composable
fun <V> RadioGroupPreference(
    title: String,
    description: String? = null,
    icon: @Composable (() -> Unit)?,
    choices: List<Choice<out V>>,
    selected: V,
    onSelect: (V) -> Unit,
    modifier: Modifier = Modifier
) {
    PreferenceItem(
        title = title, modifier = modifier,
        description = description,
        icon = icon
    ) {
        RadioGroup(
            choices = choices,
            selected = selected,
            onSelect = onSelect
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RadioGroupPreferencePreview() = Surface {
    val context = LocalContext.current
    val dataStore = PreferenceDataStoreFactory.create(
        scope = CoroutineScope(EmptyCoroutineContext),
        produceFile = { context.filesDir.resolve("preview_settings.preferences_datastore") }
    )
    val radioGroupPreferenceKey = booleanPreferencesKey("radio_group_preference")
    val choices = listOf(
        Choice(true, "Option 1"),
        Choice(false, "Option 2"),
        Choice(false, "Option 3")
    )
    RadioGroupPreference(
        title = "Title",
        icon = {},
        description = "",
        dataStore = dataStore,
        preferenceKey = radioGroupPreferenceKey,
        choices = choices,
    )
}