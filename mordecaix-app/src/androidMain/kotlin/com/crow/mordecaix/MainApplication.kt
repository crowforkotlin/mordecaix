package com.crow.mordecaix

import com.crow.mordecaix.base.application.BaseApp
import com.crow.mordecaix.zipline.log.AndroidZipline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainApplication : BaseApp() {

    override fun onCreate() {
        super.onCreate()
        initializeApplication()
        AndroidZipline(this, CoroutineScope(Dispatchers.IO)).start()
    }
}