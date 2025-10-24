package com.crow.mordecaix.zipline

import app.cash.zipline.Zipline
import com.crow.mordecaix.zipline.services.BaseLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val zipline by lazy { Zipline.get() }

@OptIn(ExperimentalJsExport::class)
@JsExport
fun main() {
  zipline.bind<BaseLogger>(name = "JsLog", instance = JsLogger())

  CoroutineScope(Dispatchers.Default).launch {
    delay(1000L)
  }
}