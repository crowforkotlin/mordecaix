package com.crow.mordecaix.base.common

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.crow.mordecaix.base.extensions.baseDataStore
import com.crow.mordecaix.base.extensions.decode
import com.crow.mordecaix.base.extensions.encode


actual fun setString(key: String, value: String) { baseDataStore.encode(stringPreferencesKey(key), value) }
actual fun setInt(key: String, value: Int) { baseDataStore.encode(intPreferencesKey(key), value) }
actual fun setLong(key: String, value: Long) { baseDataStore.encode(longPreferencesKey(key), value) }
actual fun setFloat(key: String, value: Float) { baseDataStore.encode(floatPreferencesKey(key), value) }
actual fun setBoolean(key: String, value: Boolean) { baseDataStore.encode(booleanPreferencesKey(key), value) }
actual fun getString(key: String): String? = baseDataStore.decode(stringPreferencesKey(key))
actual fun getInt(key: String): Int? = baseDataStore.decode(intPreferencesKey(key))
actual fun getLong(key: String): Long? = baseDataStore.decode(longPreferencesKey(key))
actual fun getFloat(key: String): Float? = baseDataStore.decode(floatPreferencesKey(key))
actual fun getBoolean(key: String): Boolean? = baseDataStore.decode(booleanPreferencesKey(key))