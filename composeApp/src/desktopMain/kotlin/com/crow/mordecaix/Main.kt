package com.crow.mordecaix

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Dimension

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun main() = application {
    initializeApplication()
    Window(
        onCloseRequest = ::exitApplication,
        title = "MordecaiX",
    ) {
        window.minimumSize = Dimension(352,320)
        App()
    }
}