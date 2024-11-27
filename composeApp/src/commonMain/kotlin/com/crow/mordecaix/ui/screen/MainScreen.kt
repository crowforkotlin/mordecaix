package com.crow.mordecaix.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.crow.mordecaix.common.Platform
import com.crow.mordecaix.common.getString
import com.crow.mordecaix.common.platform
import com.crow.mordecaix.common.setString
import com.crow.mordecaix.extensions.measureTimeNotnull
import com.crow.mordecaix.ui.component.RippleRoundedFillBox
import mordecaix.composeapp.generated.resources.Res
import mordecaix.composeapp.generated.resources.history
import org.jetbrains.compose.resources.stringResource

val items = listOf(MordecaiXScreen.HistoryScreen, MordecaiXScreen.DiscoverScreen, MordecaiXScreen.BookshelfScreen, MordecaiXScreen.SettingScreen)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(windowSize: WindowSizeClass, onClick: () -> Unit) {
    val navController: NavHostController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val state = rememberLazyListState()
    if (windowSize.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
        AnimatedContent {
            Row {
                NavigationRail {
                    items.fastForEachIndexed { index, item ->
                        RippleRoundedFillBox(
                            modifierAfter = Modifier.padding(20.dp),
                            selected = item == navBackStackEntry?.destination?.route,
                            isSelectEnable = true,
                            innerColor = Color.Gray.copy(alpha = 0.6f),
                            outlineColor = Color.Green.copy(alpha = 0.6f),
                            onClick = {
                                setString("StartDestinationScreen", item)
                                navController.navigate(item) {
                                    // 防止在相同目的地之间重复导航
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    // 避免重新创建以前的目标
                                    launchSingleTop = true
                                    // 恢复之前的导航状态
                                    restoreState = true
                                }
                            }
                        ) {
                            when(item) {
                                MordecaiXScreen.HistoryScreen -> { Icon(imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.Black) }
                                MordecaiXScreen.DiscoverScreen -> { Icon(imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.Black) }
                                MordecaiXScreen.BookshelfScreen -> { Icon(imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.Black) }
                                MordecaiXScreen.SettingScreen -> { Icon(imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.Black) }
                            }
                        }
                    }
                }
                MainNavHost(state, navController, windowSize)
            }
        }
    } else {
        AnimatedContent {
            if(platform == Platform.Desktop) {
                MainNavHost(state, navController, windowSize)
            } else {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    stringResource(Res.string.history),
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                )
                            },
                        )
                    },
                    bottomBar = {
                        BottomAppBar {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    selected = item == navBackStackEntry?.destination?.route,
                                    onClick = {
                                        setString("StartDestinationScreen", item)
                                        navController.navigate(item) {
                                            // 防止在相同目的地之间重复导航
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            // 避免重新创建以前的目标
                                            launchSingleTop = true
                                            // 恢复之前的导航状态
                                            restoreState = true
                                        }
                                    },
                                    colors = NavigationBarItemDefaults.colors(),
                                    icon = {
                                        when(item) {
                                            MordecaiXScreen.HistoryScreen -> { Icon(imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.Black) }
                                            MordecaiXScreen.DiscoverScreen -> { Icon(imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.Black) }
                                            MordecaiXScreen.BookshelfScreen -> { Icon(imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.Black) }
                                            MordecaiXScreen.SettingScreen -> { Icon(imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.Black) }
                                        }
                                    },
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    MainNavHost(state, navController, windowSize, Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainNavHost(
    state: LazyListState = rememberLazyListState(),
    navController: NavHostController,
    windowSize: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    val currentScreen = measureTimeNotnull { getString("StartDestinationScreen") ?: MordecaiXScreen.HistoryScreen }
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = currentScreen
    ) {
        composableRoute(route = MordecaiXScreen.HistoryScreen) {
            HistoryScreen(windowSize)
        }
        composableRoute(route = MordecaiXScreen.DiscoverScreen) {
            DiscoverScreen()
        }
        composableRoute(route = MordecaiXScreen.BookshelfScreen) {
            BookshelfScreen()
        }
        composableRoute(route = MordecaiXScreen.SettingScreen) {
            SettingScreen(state, windowSize)
        }
    }
}