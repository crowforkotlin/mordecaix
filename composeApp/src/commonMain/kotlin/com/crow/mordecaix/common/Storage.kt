package com.crow.mordecaix.common

expect fun setString(key: String, value: String)
expect fun setInt(key: String, value: Int)
expect fun setLong(key: String, value: Long)
expect fun setFloat(key: String, value: Float)
expect fun setBoolean(key: String, value: Boolean)
expect fun getString(key: String): String?
expect fun getInt(key: String): Int?
expect fun getLong(key: String): Long?
expect fun getFloat(key: String): Float?
expect fun getBoolean(key: String): Boolean?