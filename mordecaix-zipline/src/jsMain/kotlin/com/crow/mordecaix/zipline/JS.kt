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
  zipline.bind<LogService>("JsLog", JsLog())

  // Or wait 1ms
  println("mainaasdasdasd")
  CoroutineScope(Dispatchers.Default).launch {
    delay(500)

//    val androidLog = zipline.take<LogService>("androidLog")
//    androidLog.log("hello from kotlin.................js!")
  }
  /*CoroutineScope(Dispatchers.Default).launch {
    delay(500)

    val androidLog = zipline.take<LogService>("androidLog")
    androidLog.log("hello from kotlin.................js!")

    zipline.take<ReqService>("request")
      .get("https://gitee.com/badwuya/as-the-black-wings-slowly-fell/raw/main/template.json")
  }*/
}


class JsLog : LogService {
  override fun log(content: String) {
    console.info("[JS Log] $content")
    val hostLog = zipline.take<LogService>("logger")
    hostLog.log("hello from kotlin/js!")
  }
}