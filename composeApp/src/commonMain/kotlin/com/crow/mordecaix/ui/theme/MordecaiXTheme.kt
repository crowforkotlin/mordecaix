package com.crow.mordecaix.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun MordecaiXTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            primary = BLUEF9,
            onPrimary = Color.Black,
            surface = BLUEF9,
            onSurface = Color.Black
        ),
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes.copy(
            small = RoundedCornerShape(6.dp),
            medium = RoundedCornerShape(8.dp),
            large = RoundedCornerShape(10.dp)
        ),
        content = content
    )
}