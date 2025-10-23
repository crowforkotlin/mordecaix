package com.crow.mordecaix.zipline

import app.cash.zipline.Zipline
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
  println("awaww")
    delay(1000L)
    println("awaww 222222222222222222")
    zipline.take<BaseLogger>(name = "logger333").info("ashkdashdkjasdjksahkjdhkjashdkjsahkjdhka -----------------------------")
  }
}