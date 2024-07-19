package com.buller.wweather.presentation.home

import android.graphics.PorterDuff
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.buller.wweather.domain.model.WeatherData
import com.buller.wweather.domain.model.WeatherInfo
import com.buller.wweather.domain.model.DataForChart
import com.buller.wweather.domain.model.PreferencesState
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.of
import com.patrykandpatrick.vico.compose.common.shader.color
import com.patrykandpatrick.vico.compose.common.shader.component
import com.patrykandpatrick.vico.compose.common.shader.verticalGradient
import com.patrykandpatrick.vico.compose.common.shape.dashed
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.axis.AxisPosition
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.shader.DynamicShader
import com.patrykandpatrick.vico.core.common.shader.TopBottomShader
import com.patrykandpatrick.vico.core.common.shape.Shape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

@Composable
fun Chart(weatherInfo: WeatherInfo?, prefUiState: PreferencesState, modifier: Modifier = Modifier) {
    weatherInfo?.weatherDatePerHour?.let { data ->
        val weatherData = getPoints(data, prefUiState)
        val modelProducer = remember { CartesianChartModelProducer.build() }
        LaunchedEffect(Unit) {
            withContext(Dispatchers.Default) {
                modelProducer.tryRunTransaction {
                    lineSeries {
                        series(weatherData.listPoints)
                    }
                }
            }
        }
        ComposeChart(modelProducer = modelProducer, modifier = modifier)
    }
}

@Composable
fun ComposeChart(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier
) {
    val marker = rememberMarker()

    Box(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    ) {
        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberLineCartesianLayer(
                    listOf(
                        createLineSpec(
                            listOf(
                                Color.Red,
                                Color.Blue
                            )
                        )
                    )
                ),
                startAxis = createStartAxis(),
                bottomAxis = createBottomAxis(),
            ),
            modelProducer = modelProducer,
            modifier = modifier,
            marker = marker,
        )
    }
}

@Composable
private fun createLineSpec(colors: List<Color>): LineCartesianLayer.LineSpec {
    return rememberLineSpec(
        shader = TopBottomShader(
            DynamicShader.color(colors[0]),
            DynamicShader.color(colors[1]),
        ),
        backgroundShader = TopBottomShader(
            DynamicShader.compose(
                DynamicShader.component(
                    componentSize = 6.dp,
                    component =
                    rememberShapeComponent(
                        shape = Shape.Pill,
                        color = colors[0],
                        margins = Dimensions.of(1.dp),
                    ),
                ),
                DynamicShader.verticalGradient(
                    arrayOf(Color.Black, Color.Transparent),
                ),
                PorterDuff.Mode.DST_IN,
            ),
            DynamicShader.compose(
                DynamicShader.component(
                    componentSize = 5.dp,
                    component =
                    rememberShapeComponent(
                        shape = Shape.Rectangle,
                        color = colors[1],
                        margins = Dimensions.of(horizontal = 2.dp),
                    ),
                    checkeredArrangement = false,
                ),
                DynamicShader.verticalGradient(
                    arrayOf(Color.Transparent, Color.Black),
                ),
                PorterDuff.Mode.DST_IN,
            ),
        ),

        )
}

@Composable
private fun createStartAxis(): VerticalAxis<AxisPosition.Vertical.Start> {
    return rememberStartAxis(
        label =
        rememberAxisLabelComponent(
            color = MaterialTheme.colorScheme.onBackground,
            background =
            rememberShapeComponent(
                shape = Shape.Pill,
                color = Color.Transparent,
                strokeColor = MaterialTheme.colorScheme.outlineVariant,
                strokeWidth = 1.dp,
            ),
            padding = Dimensions.of(horizontal = 6.dp, vertical = 2.dp),
            margins = Dimensions.of(start = 4.dp, end = 8.dp),
        ),
        axis = null,
        tick = null,
        guideline =
        rememberLineComponent(
            color = MaterialTheme.colorScheme.outlineVariant,
            shape =
            remember {
                Shape.dashed(
                    shape = Shape.Pill,
                    dashLength = 4.dp,
                    gapLength = 8.dp,
                )
            },
        ),
        itemPlacer = remember { AxisItemPlacer.Vertical.count(count = { 5 }) },
        valueFormatter = { value, _, _ ->
            val v = value.roundToInt()
            "$v°"
        },
    )
}

@Composable
private fun createBottomAxis(): HorizontalAxis<AxisPosition.Horizontal.Bottom> {
    return rememberBottomAxis(
        guideline = null,
        itemPlacer =
        remember {
            AxisItemPlacer.Horizontal.default(
                spacing = 3, addExtremeLabelPadding = true
            )
        },
        valueFormatter = { value, _, _ ->
            "${value.toInt()}h"
        }
    )
}

private fun getPoints(
    data: Map<Int, List<WeatherData>>,
    prefUiState: PreferencesState
): DataForChart {
    val list = data[0]
    val points = mutableListOf<Int>()
    if (prefUiState.isCelsius) {
        list?.forEach {
            val temp = it.temperatureC
            points.add(temp)
        }
    } else {
        list?.forEach {
            val temp = it.temperatureF
            points.add(temp)
        }
    }
    return DataForChart(points)
}