package com.crow.mordecaix.base.common

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.crow.mordecaix.base.application.app
import com.crow.mordecaix.base.common.BaseConstans.DataStoreFileName

actual fun createDataStore() : DataStore<Preferences> {
    return createDataStore(
        producePath = { app.filesDir.resolve(DataStoreFileName).absolutePath }
    )
}