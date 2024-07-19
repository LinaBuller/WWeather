package com.buller.wweather.presentation.settings.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Choice<V>(val value: V, val displayValue: String = value.toString())

@Composable
fun <V> RadioGroup(
    choices: List<Choice<out V>>,
    selected: V,
    onSelect: (V) -> Unit
) {
    Column {
        choices.forEach { choice ->
            val isSelected = selected == choice.value
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) Color.Blue else Color.Transparent,
                animationSpec = tween(durationMillis = 200), label = ""
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else Color.Black,
                animationSpec = tween(durationMillis = 200), label = ""
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .selectable(
                        role = Role.RadioButton,
                        selected = isSelected,
                        onClick = {
                            onSelect(choice.value)
                        }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                        .background(backgroundColor)
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .align(Alignment.Center)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = choice.displayValue,
                    color = textColor
                )
            }
        }
    }
}

@Preview
@Composable
private fun RadioGroupPreview() = Surface {
    val choices = listOf(
        Choice(4564),
        Choice(2456),
        Choice(null)
    )

    var state by remember {
        mutableStateOf<Int?>(2)
    }

    RadioGroup(
        choices = choices,
        selected = state,
        onSelect = { state = it }
    )
}