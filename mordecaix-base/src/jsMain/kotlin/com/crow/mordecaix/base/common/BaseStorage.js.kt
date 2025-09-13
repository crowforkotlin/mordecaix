package com.crow.mordecaix.base.common


import com.crow.mordecaix.base.extensions.SHIFT_32
import com.crow.mordecaix.base.extensions.SHIFT_64
import com.crow.mordecaix.base.extensions.decodeShift
import com.crow.mordecaix.base.extensions.encodeShift
import kotlinx.browser.localStorage
import org.w3c.dom.get
import org.w3c.dom.set

actual fun setString(key: String, value: String) { localStorage[encodeShift(key, SHIFT_64)] = encodeShift(value, SHIFT_32) }
actual fun setInt(key: String, value: Int) { localStorage[encodeShift(key, SHIFT_64)] = encodeShift(value.toString(), SHIFT_32) }
actual fun setLong(key: String, value: Long) { localStorage[encodeShift(key, SHIFT_64)] = encodeShift(value.toString(), SHIFT_32) }
actual fun setFloat(key: String, value: Float) { localStorage[encodeShift(key, SHIFT_64)] = encodeShift(value.toString(), SHIFT_32) }
actual fun setBoolean(key: String, value: Boolean) { localStorage[encodeShift(key, SHIFT_64)] = encodeShift(value.toString(), SHIFT_32) }
actual fun getString(key: String): String? { return runCatching { decodeShift(localStorage[encodeShift(key, SHIFT_64)] ?: return null, SHIFT_32) }.getOrNull() }
actual fun getInt(key: String): Int? { return runCatching {  (decodeShift(localStorage[encodeShift(key, SHIFT_64)] ?: return null, SHIFT_32)).toInt() }.getOrNull() }
actual fun getLong(key: String): Long? { return runCatching {  (decodeShift(localStorage[encodeShift(key, SHIFT_64)] ?: return null, SHIFT_32)).toLong() }.getOrNull() }
actual fun getFloat(key: String): Float? { return runCatching {  (decodeShift(localStorage[encodeShift(key, SHIFT_64)] ?: return null, SHIFT_32)).toFloat() }.getOrNull() }
actual fun getBoolean(key: String): Boolean? { return runCatching {  (decodeShift(localStorage[encodeShift(key, SHIFT_64)] ?: return null, SHIFT_32)).toBoolean() }.getOrNull() }