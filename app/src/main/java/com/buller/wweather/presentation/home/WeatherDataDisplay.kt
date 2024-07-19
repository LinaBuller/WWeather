package com.buller.wweather.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeatherDataDisplay(
    value: Int,
    unit: String,
    modifier: Modifier = Modifier,

) {
    Column (modifier = modifier, horizontalAlignment = Alignment.Start) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "$value", fontSize = 20.sp)
        Text(text = unit)
    }
}


