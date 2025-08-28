@file:Suppress("ConstPropertyName")

package com.crow.mordecaix.ui.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import com.crow.mordecaix.ui.viewmodel.AppViewModel
import org.koin.compose.viewmodel.koinViewModel

data object MordecaiXScreen {
    const val MainScreen = "MainScreen"
    const val HistoryScreen = "HistoryScreen"
    const val DiscoverScreen = "DiscoverScreen"
    const val BookshelfScreen = "BookshelfScreen"
    const val SettingScreen = "SettingScreen"
    const val InfoScreen = "InfoScreen"
}

@Composable
fun MordecaiXApp(
    windowSize: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
    viewmodel: AppViewModel = koinViewModel<AppViewModel>(),
    navController: NavHostController = rememberNavController()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = MordecaiXScreen.MainScreen,
        ) {
            composableRoute(route = MordecaiXScreen.MainScreen) {
                MainScreen(windowSize = windowSize) {
                    navController.navigate(MordecaiXScreen.InfoScreen)
                }
            }
            composableRoute(route = MordecaiXScreen.InfoScreen) { InfoScreen(windowSize = windowSize) }
        }
    }
}

fun NavGraphBuilder.composableRoute(
    route: String, content: @Composable() (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    composable(
        route = route,
        enterTransition = { fadeIn(tween(durationMillis = 200, easing = FastOutSlowInEasing)) },
        exitTransition = { fadeOut(tween(durationMillis = 200, easing = FastOutSlowInEasing)) },
        popEnterTransition = { fadeIn(tween(durationMillis = 200, easing = FastOutSlowInEasing)) },
        popExitTransition = { fadeOut(tween(durationMillis = 200, easing = FastOutSlowInEasing)) },
        content = content
    )
}

@Composable
fun AnimatedContent(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    AnimatedVisibility(
        modifier = modifier,
        visible = visible, enter = fadeIn(), exit = fadeOut()
    ) {
        content()
    }
}