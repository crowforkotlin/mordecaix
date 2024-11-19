package com.crow.mordecaix.common

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import kotlinx.coroutines.flow.Flow

expect fun getAppDatabase() : AppDatabase

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

@Database(entities = [TodoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): TodoDao
}

@Dao
interface TodoDao {
    @Insert
    suspend fun insert(item: TodoEntity)

    @Query("SELECT count(*) FROM TodoEntity")
    suspend fun count(): Int

    @Query("SELECT * FROM TodoEntity")
    fun getAllAsFlow(): Flow<List<TodoEntity>>
}

@Entity
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String
)