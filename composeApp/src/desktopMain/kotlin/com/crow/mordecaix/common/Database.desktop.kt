package com.crow.mordecaix.common

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import java.io.File

actual fun getAppDatabase(): AppDatabase { return getDatabaseBuilder().build() }
fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File("${System.getProperty("user.dir")}/database", "my_room.db")
    return Room.databaseBuilder<AppDatabase>(name = dbFile.absolutePath).also {
        it.setDefaults()
        it.setQueryCoroutineContext(Dispatchers.IO)
    }
}
