package com.crow.mordecaix.common

import kotlinx.browser.localStorage
import org.w3c.dom.get
import org.w3c.dom.set

actual fun setString(key: String, value: String) { localStorage[key] = value }
actual fun setInt(key: String, value: Int) { localStorage[key] = value.toString() }
actual fun setLong(key: String, value: Long) { localStorage[key] = value.toString() }
actual fun setFloat(key: String, value: Float) { localStorage[key] = value.toString() }
actual fun setBoolean(key: String, value: Boolean) { localStorage[key] = value.toString() }
actual fun getString(key: String): String? { return runCatching { localStorage[key] }.getOrNull() }
actual fun getInt(key: String): Int? { return runCatching {  localStorage[key]?.toInt() }.getOrNull() }
actual fun getLong(key: String): Long? { return runCatching {  localStorage[key]?.toLong() }.getOrNull() }
actual fun getFloat(key: String): Float? { return runCatching {  localStorage[key]?.toFloat() }.getOrNull() }
actual fun getBoolean(key: String): Boolean? { return runCatching {  localStorage[key]?.toBoolean() }.getOrNull() }


