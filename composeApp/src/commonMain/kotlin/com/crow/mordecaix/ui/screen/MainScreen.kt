package com.crow.mordecaix.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Create
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import kotlinx.collections.immutable.toImmutableList

val list = (1..200).toImmutableList()

@Composable
fun MainScreen(windowSize: WindowSizeClass) {
    val navController: NavHostController = rememberNavController()
    val items = listOf(Icons.Rounded.Create, Icons.Rounded.Add, Icons.Rounded.AddCircle)
    val state = rememberLazyListState()
    if (windowSize.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
        AnimatedContent {
            Row {
                NavigationRail {
                    items.forEach {
                        NavigationRailItem(
                            selected = true,
                            onClick = { },
                            icon = {
                                FloatingActionButton(
                                    modifier = Modifier.padding(10.dp),
                                    shape = MaterialTheme.shapes.small,
                                    contentColor = MaterialTheme.colors.onPrimary,
                                    onClick = { },
                                    content = {
                                        Icon(imageVector = it, contentDescription = null)
                                    },
                                )
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
                            Column {
                                Text("title", style = MaterialTheme.typography.h5)
                                Text("subtitle", style = MaterialTheme.typography.subtitle1)
                            }
                        },
                        backgroundColor = MaterialTheme.colors.primary
                    )
                },
                bottomBar = {
                    BottomNavigation() {
                        items.forEach {
                            BottomNavigationItem(
                                selected = true,
                                onClick = {},
                                icon = { Icon(imageVector = it, contentDescription = null) }
                            )
                        }
                    }
                }
            ) {
                MainNavHost(state, navController, windowSize)
            }
        }
    }
}

@Composable
fun MainNavHost(
    state: LazyListState = rememberLazyListState(),
    navController: NavHostController,
    windowSize: WindowSizeClass
) {
    NavHost(
        navController = navController,
        startDestination = MordecaiXScreen.ComicInfoScreen.name
    ) {
        composableRoute(route = MordecaiXScreen.ComicInfoScreen.name) {
            ComicInfoScreen(state, windowSize)
        }
    }
}