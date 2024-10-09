package com.crow.mordecaix.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass


@Composable
fun SettingScreen(state1: LazyListState = rememberLazyListState(), windowSize: WindowSizeClass) {
    Row {
        LazyColumn(state = state1, modifier = Modifier.fillMaxSize().weight(1f)) {
            items(list.size) {
                Text(text = list[it].toString())
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize().weight(1f)) {
            items(list.size) {
                Text(text = list[it].toString())
            }
        }
    }
}