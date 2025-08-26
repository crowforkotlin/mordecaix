package com.crow.mordecaix.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val darkScheme = darkColorScheme(
    primary = BLUEF9,
    onPrimary = Color.Black,
    surface = BLUEF9,
    onSurface = Color.Black,
    surfaceContainer = BLUEF9,
    secondaryContainer = BLUE500,
)
private val lightScheme = lightColorScheme(
    primary = BLUEF9,
    onPrimary = Color.Black,
    surface = BLUEF9,
    onSurface = Color.Black,
    surfaceContainer = BLUEF9,
    secondaryContainer = BLUE500,
)

@Composable
fun MordecaiXTheme(mordecaiTheme: Int = 0, isDark: Boolean = false, content: @Composable () -> Unit) {
    if (isDark) {
        MaterialTheme(
            colorScheme = darkScheme,
            typography = MaterialTheme.typography,
            shapes = MaterialTheme.shapes.copy(
                small = RoundedCornerShape(6.dp),
                medium = RoundedCornerShape(8.dp),
                large = RoundedCornerShape(10.dp)
            ),
            content = content
        )
    } else {
        MordecaiXTheme(mordecaiTheme, content)
    }
}

@Composable
private fun MordecaiXTheme(mordecaiTheme: Int, content: @Composable () -> Unit) {
    when(mordecaiTheme) {
        0 -> {
            MaterialTheme(
                colorScheme = lightScheme,
                typography = MaterialTheme.typography,
                shapes = MaterialTheme.shapes.copy(
                    small = RoundedCornerShape(6.dp),
                    medium = RoundedCornerShape(8.dp),
                    large = RoundedCornerShape(10.dp)
                ),
                content = content
            )
        }
    }
}