package com.crow.mordecaix

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.crow.mordecaix.model.di.networkModule
import org.koin.core.context.startKoin

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun main() = application {
    startKoin {
        modules(
            networkModule
        )
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "",
    ) {
        val windowSize = calculateWindowSizeClass()
        App(windowSize = windowSize)
    }
}