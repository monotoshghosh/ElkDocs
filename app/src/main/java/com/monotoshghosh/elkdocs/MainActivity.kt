package com.monotoshghosh.elkdocs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.monotoshghosh.elkdocs.ui.theme.ElkDocsTheme
import kotlinx.coroutines.delay
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElkDocsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black // Set background color to black
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        AnalogClock() // Display the analog clock in the center
                    }
                }
            }
        }
    }
}

@Composable
fun AnalogClock() {
    val calendar = Calendar.getInstance()
    var seconds by remember { mutableStateOf(calendar.get(Calendar.SECOND)) }
    var minutes by remember { mutableStateOf(calendar.get(Calendar.MINUTE)) }
    var hours by remember { mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY)) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L) // Update every second
            calendar.timeInMillis = System.currentTimeMillis()
            seconds = calendar.get(Calendar.SECOND)
            minutes = calendar.get(Calendar.MINUTE)
            hours = calendar.get(Calendar.HOUR_OF_DAY)
        }
    }

    val boundarySize = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        while (true) {
            boundarySize.animateTo(1.1f, animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            ))
            boundarySize.animateTo(1f, animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing))
        }
    }

    Canvas(modifier = Modifier.size(300.dp)) {
        val radius = size.minDimension / 2
        val centerX = size.width / 2
        val centerY = size.height / 2

        // Draw pulsating boundary
        drawCircle(
            color = Color(0xFF33691E), // Adjust color as needed
            radius = radius * boundarySize.value,
            center = androidx.compose.ui.geometry.Offset(centerX, centerY),
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = 8.dp.toPx()
            )
        )

        // Draw clock circle
        drawCircle(
            color = Color.White,
            radius = radius,
            center = androidx.compose.ui.geometry.Offset(centerX, centerY),
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = 4.dp.toPx()
            )
        )

        // Calculate angles for the clock hands
        val hourAngle = (hours % 12 + minutes / 60f) * 30 * (PI / 180)
        val minuteAngle = (minutes + seconds / 60f) * 6 * (PI / 180)
        val secondAngle = seconds * 6 * (PI / 180)

        // Draw hour hand (red)
        drawLine(
            color = Color.Red,
            start = androidx.compose.ui.geometry.Offset(centerX, centerY),
            end = androidx.compose.ui.geometry.Offset(
                centerX + radius * 0.5f * cos(hourAngle - PI / 2).toFloat(),
                centerY + radius * 0.5f * sin(hourAngle - PI / 2).toFloat()
            ),
            strokeWidth = 8.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Draw minute hand (blue)
        drawLine(
            color = Color.Blue,
            start = androidx.compose.ui.geometry.Offset(centerX, centerY),
            end = androidx.compose.ui.geometry.Offset(
                centerX + radius * 0.7f * cos(minuteAngle - PI / 2).toFloat(),
                centerY + radius * 0.7f * sin(minuteAngle - PI / 2).toFloat()
            ),
            strokeWidth = 6.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Draw second hand (yellow)
        drawLine(
            color = Color.Yellow,
            start = androidx.compose.ui.geometry.Offset(centerX, centerY),
            end = androidx.compose.ui.geometry.Offset(
                centerX + radius * 0.9f * cos(secondAngle - PI / 2).toFloat(),
                centerY + radius * 0.9f * sin(secondAngle - PI / 2).toFloat()
            ),
            strokeWidth = 4.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AnalogClockPreview() {
    ElkDocsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black // Set background color to black
        ) {
            Box(contentAlignment = Alignment.Center) {
                AnalogClock() // Display the analog clock in the center
            }
        }
    }
}
