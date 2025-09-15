package com.crow.mordecaix.zipline

class DesktopLogger : LogService {
  override fun log(content: String) {
    println("[Desktop] : $content")
  }
}