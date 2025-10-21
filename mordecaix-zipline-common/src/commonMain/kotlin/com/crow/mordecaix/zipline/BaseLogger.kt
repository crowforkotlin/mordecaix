package com.crow.mordecaix.zipline

import app.cash.zipline.ZiplineService

interface BaseLogger : ZiplineService {
    fun info(message: String) {}
    fun error(message: String) {}
    fun warn(message: String) {}
}