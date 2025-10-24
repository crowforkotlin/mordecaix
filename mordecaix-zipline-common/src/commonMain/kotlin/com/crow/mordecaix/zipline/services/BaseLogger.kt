package com.crow.mordecaix.zipline.services

import app.cash.zipline.ZiplineService

interface BaseLogger : BaseServices {
    fun info(message: String) {}
    fun error(message: String) {}
    fun warn(message: String) {}
}