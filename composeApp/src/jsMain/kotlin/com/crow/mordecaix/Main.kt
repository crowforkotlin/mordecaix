package com.crow.mordecaix

import androidx.compose.material.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() = onWasmReady {
//    initializeApplication()
//    ComposeViewport(document.body!!) {
//        App()
//    }
    ComposeViewport(document.body!!) {
        Text("1231231231231232123123")
    }
}