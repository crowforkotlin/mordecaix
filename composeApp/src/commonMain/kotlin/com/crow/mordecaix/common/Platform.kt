package com.crow.mordecaix.common

expect val commonPlatform : Platform

sealed class Platform(val name: String) {
    data object Android : Platform("Android")
    data object Apple : Platform("Apple")
    data object Desktop : Platform("Desktop")
    data object Web : Platform("Web")
}