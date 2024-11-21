package com.crow.mordecaix.extensions

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL

private class AppDatabaseCallback : RoomDatabase.Callback() {
    override fun onOpen(connection: SQLiteConnection) {
        connection.apply {
            execSQL("PRAGMA synchronous = NORMAL")
        }
    }
}

private fun <T : RoomDatabase> RoomDatabase.Builder<T>.addFallbackInDebugOnly(): RoomDatabase.Builder<T> {
    fallbackToDestructiveMigration(true)
    return this
}

fun <T : RoomDatabase> RoomDatabase.Builder<T>.setDefaults(): RoomDatabase.Builder<T> = this.apply {
    setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING) //enabling WAL https://www.sqlite.org/wal.html
    setDriver(BundledSQLiteDriver())
    addCallback(AppDatabaseCallback())
    addFallbackInDebugOnly()
}