package com.crow.mordecaix

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.Navigator
import com.crow.mordecaix.ui.screen.horizontal.ContainerHorizontalScreen
import com.crow.mordecaix.ui.screen.vertical.ContainerVerticalScreen
import com.crow.mordecaix.ui.theme.MordecaiTheme


@Composable
fun App(windowSize: WindowSizeClass) {
    MordecaiTheme {
        if (windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) {
            onExpandApp()
        } else {
            onMediumApp()
        }
    }
}

@Composable
fun onExpandApp() {
    println("Expand")
    AnimatedContent {
        Navigator(ContainerVerticalScreen())
    }
}

@Composable
fun onMediumApp() {
    println("Medium")
    AnimatedContent {
        Navigator(ContainerHorizontalScreen())
    }
}

@Composable
fun AnimatedContent(content: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
        exit = fadeOut(animationSpec = tween(durationMillis = 1000))
    ) {
        content()
    }
}


