package com.crow.mordecaix.zipline

import com.crow.mordecaix.zipline.services.BaseLogger

class JsLogger : BaseLogger {
  override fun info(message: String) { console.info(message) }
  override fun error(message: String) { console.error(message) }
  override fun warn(message: String) { console.warn(message) }
}