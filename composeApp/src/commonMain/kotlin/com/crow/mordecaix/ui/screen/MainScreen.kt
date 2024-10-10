package com.crow.mordecaix.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import kotlinx.collections.immutable.toImmutableList
import mordecaix.composeapp.generated.resources.Res
import mordecaix.composeapp.generated.resources.source
import org.jetbrains.compose.resources.stringResource

val list = (1..200).toImmutableList()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(windowSize: WindowSizeClass) {
    val navController: NavHostController = rememberNavController()
    val items = listOf(Icons.Rounded.Create, Icons.Rounded.Add, Icons.Rounded.AddCircle)
    var barSelectedItem by remember { mutableStateOf(0) }
    val state = rememberLazyListState()
    if (windowSize.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
        AnimatedContent {
            Row {
                NavigationRail {
                    items.fastForEachIndexed { index, item ->
                        NavigationRailItem(
                            selected = index == barSelectedItem,
                            onClick = { barSelectedItem = index },
                            icon = {
                                Icon(imageVector = item, contentDescription = null)
                            }
                        )
                    }
                }
                MainNavHost(state, navController, windowSize)
            }
        }
    } else {
        AnimatedContent {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                stringResource(Res.string.source),
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        },
                    )
                },
                bottomBar = {
                    BottomAppBar(
                    ) {
                        items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = index == barSelectedItem,
                                onClick = { barSelectedItem = index },
                                colors = NavigationBarItemDefaults.colors(),
                                icon = { Icon(imageVector = item, contentDescription = null, tint = if(index == barSelectedItem) Color.White else Color.Black) },
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

@Composable
fun MainNavHost(
    state: LazyListState = rememberLazyListState(),
    navController: NavHostController,
    windowSize: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MordecaiXScreen.SourceScreen.name
    ) {
        composableRoute(route = MordecaiXScreen.SourceScreen.name) {
            SourceScreen(windowSize = windowSize)
        }
        composableRoute(route = MordecaiXScreen.HistoryScreen.name) {
            HistoryScreen(windowSize = windowSize)
        }
    }
}