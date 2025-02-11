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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.monotoshghosh.elkdocs.ui.theme.ElkDocsTheme
import kotlinx.coroutines.delay
import java.util.Calendar
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

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
                        AnimatedAnalogClock() // Display the animated analog clock in the center
                    }
                }
            }
        }

        // Set status bar color to black and icons to white
        val window = window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.BLACK
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
    }
}

@Composable
fun AnimatedAnalogClock() {
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

    val boundarySize1 = remember { Animatable(1f) }
    val boundarySize2 = remember { Animatable(1f) }
    val boundarySize3 = remember { Animatable(1f) }
    val topCirclePulse = remember { Animatable(1f) }
    val bottomCirclePulse = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        while (true) {
            boundarySize1.animateTo(1.1f, animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            ))
            boundarySize1.animateTo(1f, animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing))

            boundarySize2.animateTo(1.15f, animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
            ))
            boundarySize2.animateTo(1f, animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing))

            boundarySize3.animateTo(1.2f, animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing)
            ))
            boundarySize3.animateTo(1f, animationSpec = tween(durationMillis = 1400, easing = FastOutSlowInEasing))

            topCirclePulse.animateTo(1.1f, animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            ))
            topCirclePulse.animateTo(1f, animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing))

            bottomCirclePulse.animateTo(1.1f, animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            ))
            bottomCirclePulse.animateTo(1f, animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing))
        }
    }

    Box(contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(300.dp)) { // Adjust the size of the clock here
            val radius = size.minDimension / 2
            val centerX = size.width / 2
            val centerY = size.height / 2

            // Draw boundary circles with different colors and sizes
            drawCircle(
                color = Color(0xFF33691E), // Green
                radius = radius * boundarySize1.value,
                center = androidx.compose.ui.geometry.Offset(centerX, centerY),
                style = Stroke(width = 8.dp.toPx())
            )
            drawCircle(
                color = Color(0xFF00C853), // Light Green
                radius = radius * boundarySize2.value,
                center = androidx.compose.ui.geometry.Offset(centerX, centerY),
                style = Stroke(width = 8.dp.toPx())
            )
            drawCircle(
                color = Color(0xFF1DE9B6), // Teal
                radius = radius * boundarySize3.value,
                center = androidx.compose.ui.geometry.Offset(centerX, centerY),
                style = Stroke(width = 8.dp.toPx())
            )

            // Draw left pulsating circle (previously top circle) with increased size and closer position
            drawCircle(
                color = Color.White.copy(alpha = 0.7f),
                radius = radius * 0.15f * topCirclePulse.value, // Increased size
                center = androidx.compose.ui.geometry.Offset(centerX - radius * 0.55f, centerY), // Move closer to the center
                style = Stroke(width = 3.dp.toPx())
            )

            // Draw right pulsating circle (previously bottom circle) with increased size and closer position
            drawCircle(
                color = Color.White.copy(alpha = 0.7f),
                radius = radius * 0.15f * bottomCirclePulse.value, // Increased size
                center = androidx.compose.ui.geometry.Offset(centerX + radius * 0.55f, centerY), // Move closer to the center
                style = Stroke(width = 3.dp.toPx())
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

            // Draw clock circle
            drawCircle(
                color = Color.White,
                radius = radius,
                center = androidx.compose.ui.geometry.Offset(centerX, centerY),
                style = Stroke(width = 4.dp.toPx())
            )

            // Draw separate needles in the small grey circles
            val leftCircleAngle = (seconds % 60) * 6 * (PI / 180) // Top circle needle
            val rightCircleAngle = (minutes % 60) * 6 * (PI / 180) // Bottom circle needle

            // Draw needle in left pulsating circle (representing seconds)
            drawLine(
                color = Color.Yellow,
                start = androidx.compose.ui.geometry.Offset(centerX - radius * 0.55f, centerY), // Move closer to the center
                end = androidx.compose.ui.geometry.Offset(
                    centerX - radius * 0.55f + radius * 0.12f * cos(leftCircleAngle - PI / 2).toFloat(), // Adjust the needle position
                    centerY + radius * 0.12f * sin(leftCircleAngle - PI / 2).toFloat() // Adjust the needle position
                ),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Draw needle in right pulsating circle (representing minutes)
            drawLine(
                color = Color.Blue,
                start = androidx.compose.ui.geometry.Offset(centerX + radius * 0.55f, centerY), // Move closer to the center
                end = androidx.compose.ui.geometry.Offset(
                    centerX + radius * 0.55f + radius * 0.12f * cos(rightCircleAngle - PI / 2).toFloat(), // Adjust the needle position
                    centerY + radius * 0.12f * sin(rightCircleAngle - PI / 2).toFloat() // Adjust the needle position
                ),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
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
            AnimatedAnalogClock() // Display the animated analog clock in the center
        }
    }
}
