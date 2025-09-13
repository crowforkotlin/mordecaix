package com.mordecai.zipline.log

import android.util.Log
import com.mordecai.zipline.LogService

class AndroidLogger : LogService {
  override fun log(content: String) {
    Log.d("AndroidLogger", "[Android Log] $content")
  }
}