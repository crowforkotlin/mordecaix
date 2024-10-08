package com.crow.mordecaix

import android.app.Application
import com.crow.mordecaix.model.di.networkModule
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                networkModule
            )
        }
    }
}