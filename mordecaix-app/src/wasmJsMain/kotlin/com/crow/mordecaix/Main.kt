package com.crow.mordecaix

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initializeApplication()
    ComposeViewport(document.body!!) {
        App()
    }
    GlobalScope.launch {
        loadAndCallWasm()
    }
}