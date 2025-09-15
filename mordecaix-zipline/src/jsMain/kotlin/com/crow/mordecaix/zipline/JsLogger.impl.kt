package com.crow.mordecaix.zipline

class JsLoggerImpl : LogService {
  override fun log(content: String) {
    println("[Desktop] : $content")
  }
}