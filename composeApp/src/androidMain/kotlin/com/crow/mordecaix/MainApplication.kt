package com.crow.mordecaix

import android.app.Application

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeApplication()
    }
}