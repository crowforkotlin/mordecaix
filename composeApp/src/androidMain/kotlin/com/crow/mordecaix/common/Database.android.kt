package com.crow.mordecaix.common

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.crow.mordecaix.MainApplication.Companion.app
import kotlinx.coroutines.Dispatchers

actual fun getAppDatabase(): AppDatabase { return getDatabaseBuilder(app).build() }

internal fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("my_room.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    ).also {
        it.setDefaults()
        it.setQueryCoroutineContext(Dispatchers.IO)
    }
}