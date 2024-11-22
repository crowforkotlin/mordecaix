package com.crow.mordecaix.common

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.crow.mordecaix.extensions.applicationDatastore
import com.crow.mordecaix.extensions.decode
import com.crow.mordecaix.extensions.encode


actual fun setString(key: String, value: String) { applicationDatastore.encode(stringPreferencesKey(key), value) }
actual fun setInt(key: String, value: Int) { applicationDatastore.encode(intPreferencesKey(key), value) }
actual fun setLong(key: String, value: Long) { applicationDatastore.encode(longPreferencesKey(key), value) }
actual fun setFloat(key: String, value: Float) { applicationDatastore.encode(floatPreferencesKey(key), value) }
actual fun setBoolean(key: String, value: Boolean) { applicationDatastore.encode(booleanPreferencesKey(key), value) }
actual fun getString(key: String): String? = applicationDatastore.decode(stringPreferencesKey(key))
actual fun getInt(key: String): Int? = applicationDatastore.decode(intPreferencesKey(key))
actual fun getLong(key: String): Long? = applicationDatastore.decode(longPreferencesKey(key))
actual fun getFloat(key: String): Float? = applicationDatastore.decode(floatPreferencesKey(key))
actual fun getBoolean(key: String): Boolean? = applicationDatastore.decode(booleanPreferencesKey(key))