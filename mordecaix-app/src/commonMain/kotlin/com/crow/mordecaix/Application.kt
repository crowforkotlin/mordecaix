package com.crow.mordecaix

import com.crow.mordecaix.model.di.networkModule
import com.crow.mordecaix.model.di.viewmodelModule
import org.koin.core.context.startKoin

fun initializeApplication() {
    startKoin {
        modules(
            networkModule,
            viewmodelModule,
        )
    }
}