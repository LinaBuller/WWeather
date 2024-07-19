package com.buller.wweather.presentation.settings.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun SettingsSwitch(
    checked: Boolean,
    option1: String,
    option2: String,
    modifier: Modifier = Modifier,
    onOptionSelected: (Boolean) -> Unit
) {
    var isSelected = checked
    val transition = updateTransition(targetState = isSelected, label = "Switch")

    val offset by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 200) },
        label = "Offset"
    ) { isSel ->
        if (isSel) 1f else 0f
    }

    val topStartCornerRadius by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 200) },
        label = "Top Start Corner Radius"
    ) { isSel ->
        if (isSel) 25f else 15f
    }

    val topEndCornerRadius by transition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis =
                200
            )
        },
        label = "Top End Corner Radius"
    ) { isSel ->
        if (isSel) 15f else 25f
    }

    val bottomStartCornerRadius by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 200) },
        label = "Bottom Start Corner Radius"
    ) { isSel ->
        if (isSel) 25f else 15f
    }

    val bottomEndCornerRadius by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 200) },
        label = "Bottom End Corner Radius"
    ) { isSel ->
        if (isSel) 15f else 25f
    }

    val backgroundColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = 200) },
        label = "Background Color"
    ) { isSel ->
        if (isSel) Color(0xFF673AB7) else Color(0xFF00BCD4)
    }

    val textColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = 400) },
        label = "Text Color"
    ) { isSel ->
        if (isSel) Color.White else Color.Black
    }

    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = modifier
            .width(150.dp)
            .height(50.dp)
            .background(
                backgroundColor, shape = RoundedCornerShape(
                    topStart = topStartCornerRadius.dp,
                    topEnd = topEndCornerRadius.dp,
                    bottomStart = bottomStartCornerRadius.dp,
                    bottomEnd = bottomEndCornerRadius.dp
                )
            ).clickable(interactionSource = interactionSource,indication = null){
                isSelected = !isSelected
               onOptionSelected(isSelected)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = modifier
                .fillMaxHeight()
                .width(75.dp)
                .offset(x = (offset * 75).dp),
            contentAlignment = if (isSelected) Alignment.CenterEnd else Alignment.CenterStart
        ) {
            Text(
                text = if (isSelected) option2 else option1,
                color = textColor,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .animateContentSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsSwitchPreview() = Surface {
    SettingsSwitch(
        checked = false,
        option1 = "black",
        option2 = "white",
        onOptionSelected = {})
}