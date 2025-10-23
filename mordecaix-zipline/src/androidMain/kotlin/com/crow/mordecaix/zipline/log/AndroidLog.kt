@file:Suppress("SpellCheckingInspection")

package com.crow.mordecaix.zipline.log

import android.util.Log
import com.crow.mordecaix.zipline.BaseLogger

class AndroidLog : BaseLogger {
  companion object {
    private const val TAG = "mordecaix-logger"
  }

  override fun info(message: String) { Log.d(TAG, message) }
  override fun error(message: String) { Log.e(TAG, message) }
  override fun warn(message: String) { Log.w(TAG, message) }
}