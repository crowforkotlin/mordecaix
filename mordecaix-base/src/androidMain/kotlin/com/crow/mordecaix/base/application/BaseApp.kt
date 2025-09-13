package com.crow.mordecaix.base.application

import android.app.Application

val app = BaseApp.context

open class BaseApp : Application() {

    companion object { lateinit var context: Application }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}