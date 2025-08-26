package com.crow.mordecaix

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import modules.sorted
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() = onWasmReady {
    initializeApplication()
    ComposeViewport(document.body!!) {
        App()
    }
    test()
}

fun test() {
    println(sorted(arrayOf(3,2,1)))
}