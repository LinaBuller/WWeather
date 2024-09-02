package com.buller.wweather.presentation.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.buller.wweather.R
import com.buller.wweather.domain.model.WeatherInfo
import java.time.format.DateTimeFormatter
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import co.yml.charts.common.extensions.isNotNull
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Locale.*


@Composable
fun AstronomyCard(
    modifier: Modifier = Modifier,
    weatherInfo: WeatherInfo? = null
) {
    fun convertTo24HourFormat(time12Hour: String): String {
        val inputFormat = SimpleDateFormat("hh:mm aa", US)
        val outputFormat = SimpleDateFormat("HH:mm", getDefault())
        val date = inputFormat.parse(time12Hour)
        return outputFormat.format(date!!)
    }

    fun getTimeFromZone(timestamp: Int, timeZoneId: String): String {
        val zoneId = ZoneId.of(timeZoneId)
        val local = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp.toLong()), zoneId)
        val dateFormat24h = DateTimeFormatter.ofPattern("HH:mm")
        return local.format(dateFormat24h)
    }

    fun convertTimeToProgress(riseTime: String, setTime: String, currentTime: String): Float? {
        val timeFormatter24h = DateTimeFormatter.ofPattern("HH:mm")
        val sunriseTime = LocalTime.parse(riseTime, timeFormatter24h)
        val sunsetTime = LocalTime.parse(setTime, timeFormatter24h)
        val currentTimeValue = LocalTime.parse(currentTime, timeFormatter24h)
        val sunriseMinutes = sunriseTime.hour * 60 + sunriseTime.minute
        val sunsetMinutes = sunsetTime.hour * 60 + sunsetTime.minute
        val currentMinutes = currentTimeValue.hour * 60 + currentTimeValue.minute
        return if (currentMinutes in sunriseMinutes..sunsetMinutes) {
            (currentMinutes - sunriseMinutes).toFloat() / (sunsetMinutes - sunriseMinutes).toFloat()
        } else {
            null
        }
    }

    weatherInfo?.currentWeatherData?.astroInfo.let { data ->
        if (data != null) {
            //@TODO make two different type hour format
            val sunriseTime = convertTo24HourFormat(data.sunrise)
            val sunsetTime = convertTo24HourFormat(data.sunset)
            val currentTime = getTimeFromZone(
                weatherInfo?.currentWeatherData!!.timestamp,
                weatherInfo.currentWeatherData.timeZoneId
            )
            val progress = convertTimeToProgress(
                riseTime = sunriseTime,
                setTime = sunsetTime,
                currentTime = currentTime
            )

            SunPart(
                progress = if (progress.isNotNull()) progress!! else 1.0f,
                riseTime = sunriseTime,
                setTime = sunsetTime
            )

        }
    }
}

@Composable
fun SunPart(progress: Float, riseTime: String, setTime: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ArcProgressBar(progress, riseTime = riseTime, setTime = setTime)
    }
}

@Composable
fun ArcProgressBar(progress: Float, riseTime: String, setTime: String) {

    val image = ImageVector.vectorResource(R.drawable.sunny_clear_day_icon)
    val painter = rememberVectorPainter(image = image)
    val isDarkTheme = isSystemInDarkTheme()
    Canvas(
        modifier = Modifier
            .padding(start = 48.dp, top = 24.dp, end = 48.dp)
            .fillMaxWidth()
            .height(300.dp)
    ) {
        val sweepAngle = progress * 180f
        val gradientBrush = Brush.linearGradient(
            colors = listOf(
                Color.Blue,
                Color.Yellow,
                Color.Red
            ),
            start = Offset.Zero,
            end = Offset(size.width, size.height)
        )

        //Background Arc
        drawArc(
            brush = gradientBrush,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            style = Stroke(16.dp.toPx(), cap = StrokeCap.Round),
            size = Size(size.width, size.height)
        )

        val heightArc = size.height / 2
        val horizonStart = -50f
        val horizonEnd = size.width + 50f

        val paint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = 20.sp.toPx()
            color = if (isDarkTheme) Color.White.toArgb() else Color.Black.toArgb()
        }

        val paintTime = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = 40.sp.toPx()
            color = if (isDarkTheme) Color.White.toArgb() else Color.Black.toArgb()
        }

        drawIntoCanvas {
            it.nativeCanvas.drawText(
                "Sunrise",
                horizonStart - 40f,
                heightArc + 120f,
                paint
            )
            it.nativeCanvas.drawText(
                "Sunset",
                horizonEnd - 140f,
                heightArc + 120f,
                paint
            )
            it.nativeCanvas.drawText(
                riseTime,
                horizonStart - 50f,
                heightArc + 250f,
                paintTime
            )
            it.nativeCanvas.drawText(
                setTime,
                horizonEnd - 430f,
                heightArc + 250f,
                paintTime
            )
        }

        drawLine(
            color = if (isDarkTheme) Color.White else Color.Black,
            start = Offset(horizonStart + 120f, heightArc),
            end = Offset(horizonEnd - 120f, heightArc),
            strokeWidth = 2.dp.toPx()
        )
        // Foreground Arc
        drawArc(
            color = Color.Transparent,
            startAngle = 180f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(0.dp.toPx()),
            size = Size(size.width, size.height)
        )

        val endAngle = 180f + sweepAngle
        val endX = center.x + (size.width / 2) * cos(Math.toRadians(endAngle.toDouble())).toFloat()
        val endY = center.y + (size.height / 2) * sin(Math.toRadians(endAngle.toDouble())).toFloat()

        with(painter) {
            drawIntoCanvas {
                val scaledWidth = intrinsicSize.width * 0.1f
                val scaledHeight = intrinsicSize.height * 0.1f
                it.translate(endX - scaledWidth / 2, endY - scaledHeight / 2)
                it.scale(0.1f, 0.1f)
                draw(intrinsicSize)
            }
        }
    }
}
