package com.crow.mordecaix.zipline.log

import android.util.Log
import com.crow.mordecaix.zipline.LogService

class AndroidLogger : LogService {
  override fun log(content: String) {
    Log.d("AndroidLogger", "[Android Log] $content")
  }
}