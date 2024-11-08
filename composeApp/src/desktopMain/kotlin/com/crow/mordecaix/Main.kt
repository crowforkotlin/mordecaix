package com.crow.mordecaix

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun main() = application {
    initializeApplication()
    val state = rememberWindowState()
    Window(
        state = state,
        onCloseRequest = ::exitApplication,
        title = "MordecaiX",
    ) {
        window.minimumSize = Dimension(352,320)
        App()
    }
}