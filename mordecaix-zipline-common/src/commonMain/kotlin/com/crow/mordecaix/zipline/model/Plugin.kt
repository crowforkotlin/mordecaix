package com.crow.mordecaix.zipline.model

data class Plugin(
    val tag: String,
    val name: String,
    val author: String,
    val version: String,
    val update: Long,
    val create: Long,
    val country: String
)
