package com.crow.mordecaix.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass
import com.crow.mordecaix.model.exposed.SourceModel
import org.koin.compose.getKoin

@Composable
fun SourceScreen(windowSize: WindowSizeClass) {
    val list = getKoin().get<List<SourceModel>>()
    println(list)
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(list.size) {
            Text(list[it].mName)
        }
    }
}