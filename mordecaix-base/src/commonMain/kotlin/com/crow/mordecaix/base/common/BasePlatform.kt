package com.crow.mordecaix.base.common


expect val basePlatform : BasePlatform

sealed class BasePlatform(val name: String) {
    data object Android : BasePlatform("Android")
    data object Apple : BasePlatform("Apple")
    data object Desktop : BasePlatform("Desktop")
    data object Web : BasePlatform("Web")
}