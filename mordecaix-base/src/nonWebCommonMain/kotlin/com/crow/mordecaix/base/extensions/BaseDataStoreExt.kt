@file:Suppress("unused")

package com.crow.mordecaix.base.extensions

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.crow.mordecaix.base.common.createDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * Kmp DataStore Extension
 *
 * time: 2024-11-19 17:23:59 下午 星期二
 * @author:crow
 */

val baseDataStore by lazy { createDataStore() }

suspend fun DataStore<Preferences>.getIntData(name: String) =
    data.map { preferences -> preferences[intPreferencesKey(name)] ?: 0 }.first()

suspend fun DataStore<Preferences>.getLongData(name: String) =
    data.map { preferences -> preferences[longPreferencesKey(name)] ?: 0L }.first()

suspend fun DataStore<Preferences>.getFloatData(name: String) =
    data.map { preferences -> preferences[floatPreferencesKey(name)] ?: 0.0f }.first()

suspend fun DataStore<Preferences>.getDoubleData(name: String) =
    data.map { preferences -> preferences[doublePreferencesKey(name)] ?: 0.00 }.first()

suspend fun DataStore<Preferences>.getBooleanData(name: String) =
    data.map { preferences -> preferences[booleanPreferencesKey(name)] ?: false }.first()

suspend fun DataStore<Preferences>.getStringData(name: String) =
    data.map { preferences -> preferences[stringPreferencesKey(name)] ?: "" }.first()

suspend fun DataStore<Preferences>.getStringSetData(name: String) =
    data.map { preferences -> preferences[stringSetPreferencesKey(name)] ?: emptySet() }.first()

suspend fun <T> DataStore<Preferences>.asyncEncode(key: Preferences.Key<T>, value: T) {
    edit { it[key] = value }
}

suspend fun <T> DataStore<Preferences>.asyncDecode(preferencesKey: Preferences.Key<T>): T? {
    return data.map { it[preferencesKey] }.first()
}

fun <T> DataStore<Preferences>.decode(key: Preferences.Key<T>): T? {
    return runBlocking {
        data.map { it[key] }.first()
    }
}

/*
* @Description: DataStore 扩展
* @author: lei
*/
fun <T> DataStore<Preferences>.encode(key: Preferences.Key<T>, value: T) {
    runBlocking { edit { it[key] = value } }
}

suspend fun <T> Preferences.Key<T>.asyncEncode(value: T) {
    baseDataStore.edit { it[this] = value }
}

suspend fun <T> Preferences.Key<T>.asyncRemove() {
    baseDataStore.edit { it.remove(this) }
}

suspend fun <T> Preferences.Key<T>.asyncDecode(): T? {
    return baseDataStore.data.map { it[this] }.firstOrNull()
}

fun <T> Preferences.Key<T>.encode(value: T) {
    runBlocking { baseDataStore.edit { it[this@encode] = value } }
}

fun <T> Preferences.Key<T>.decode(): T? {
    return runBlocking { baseDataStore.data.map { it[this@decode] }.firstOrNull() }
}

fun <T> clear() {
    runBlocking { baseDataStore.edit { it.clear() } }
}


fun <T> Preferences.Key<T>.remove(): Preferences {
    return runBlocking { baseDataStore.edit { it.remove(this@remove) } }
}
