package com.crow.mordecaix.common

import androidx.room.Room
import androidx.room.RoomDatabase
import com.crow.mordecaix.common.todo.AppDatabase
import com.crow.mordecaix.extensions.setDefaults
import kotlinx.coroutines.Dispatchers
import java.io.File

actual fun getAppDatabase(): AppDatabase { return getDatabaseBuilder().build() }

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File("${System.getProperty("user.dir")}/database", Constans.DatabaseFileName)
    return Room.databaseBuilder<AppDatabase>(name = dbFile.absolutePath).also {
        it.setDefaults()
        it.setQueryCoroutineContext(Dispatchers.IO)
    }
}
