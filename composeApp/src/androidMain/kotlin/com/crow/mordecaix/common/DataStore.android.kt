package com.crow.mordecaix.common

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.crow.mordecaix.MainApplication
import com.crow.mordecaix.common.Constans.DataStoreFileName

actual fun createDataStore() : DataStore<Preferences> {
    return createDataStore(
        producePath = { MainApplication.app.filesDir.resolve(DataStoreFileName).absolutePath }
    )
}