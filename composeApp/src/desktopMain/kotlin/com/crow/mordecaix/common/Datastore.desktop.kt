package com.crow.mordecaix.common

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

actual fun createDataStore() : DataStore<Preferences> {
    return createDataStore(
        producePath = { File("${System.getProperty("compose.application.resources.dir")}/datastore", Constans.DataStoreFileName).absolutePath  }
    )
}