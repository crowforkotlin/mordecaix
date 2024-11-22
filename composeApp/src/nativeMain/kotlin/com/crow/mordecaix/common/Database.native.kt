package com.crow.mordecaix.common

import androidx.room.Room
import androidx.room.RoomDatabase
import com.crow.mordecaix.common.todo.AppDatabase
import com.crow.mordecaix.extensions.setDefaults
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual fun getAppDatabase(): AppDatabase { return getDatabaseBuilder().build() }

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = documentDirectory() + "/my_room.db"
    return Room.databaseBuilder<AppDatabase>(name = dbFilePath).also {
        it.setDefaults()
        it.setQueryCoroutineContext(Dispatchers.IO)
    }
}


@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}