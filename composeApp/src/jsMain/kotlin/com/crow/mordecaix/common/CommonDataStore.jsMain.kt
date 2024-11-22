package com.crow.mordecaix.common


private val mMap: MutableMap<String, Any> by lazy { mutableMapOf() }


actual fun setString(key: String, value: String) { mMap[key] = value



}
actual fun setInt(key: String, value: Int) { mMap[key] = value }
actual fun setLong(key: String, value: Long) { mMap[key] = value }
actual fun setFloat(key: String, value: Float) { mMap[key] = value }
actual fun setBoolean(key: String, value: Boolean) { mMap[key] = value }
actual fun getString(key: String): String? { return runCatching { mMap[key] as? String }.getOrNull() }
actual fun getInt(key: String): Int? { return runCatching { mMap[key] as? Int }.getOrNull() }
actual fun getLong(key: String): Long? { return runCatching { mMap[key] as? Long }.getOrNull() }
actual fun getFloat(key: String): Float? { return runCatching { mMap[key] as? Float }.getOrNull() }
actual fun getBoolean(key: String): Boolean? { return runCatching { mMap[key] as? Boolean }.getOrNull() }


