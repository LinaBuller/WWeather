package com.buller.wweather.presentation.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

val Int.u: Dp
    get() = this * 4.dp

data class PreferenceSpacing(
    val itemMinHeight: Dp = 10.u,
    val itemPadding: PaddingValues = PaddingValues(horizontal = 4.u, vertical = 2.u),
    val iconPadding: PaddingValues = PaddingValues(end = 5.u),
    val actionPadding: PaddingValues = PaddingValues(start = 0.u)
)

val LocalPreferenceSpacing = compositionLocalOf { PreferenceSpacing() }