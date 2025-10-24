package com.crow.mordecaix.zipline.log.impl

import android.util.Log
import com.crow.mordecaix.zipline.services.BaseLogger

class Logger : BaseLogger {

  companion object {
    private const val TAG = "mordecaix-logger"
  }

  override fun info(message: String) { Log.d(TAG, message) }
  override fun error(message: String) { Log.e(TAG, message) }
  override fun warn(message: String) { Log.w(TAG, message) }
}