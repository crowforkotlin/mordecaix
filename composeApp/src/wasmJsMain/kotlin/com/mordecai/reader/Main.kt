package com.mordecai.reader

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.crow.mordecaix.App
import com.crow.mordecaix.initializeApplication
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initializeApplication()
    ComposeViewport(document.body!!) {
        App()
    }
}