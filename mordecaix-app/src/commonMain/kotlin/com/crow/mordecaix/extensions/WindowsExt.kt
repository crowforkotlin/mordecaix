package com.crow.mordecaix.extensions

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
inline fun windows(
    onExpanded: () -> Unit,
    onMedium: () -> Unit
) {
    val widthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    if (widthClass == WindowWidthSizeClass.EXPANDED) {
        onExpanded()
    } else {
        onMedium()
    }
}