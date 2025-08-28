package com.crow.mordecaix

import android.app.Application

class MainApplication : Application() {

    companion object { lateinit var app : MainApplication }

    override fun onCreate() {
        super.onCreate()
        app = this
        initializeApplication()
    }
}