package com.commanderpepper.advancecounter.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.commanderpepper.advancecounter.database.model.Counter

const val DATABASE_NAME = "counter"
private const val DATABASE_VERSION = 1

@Database(
    entities = [Counter::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class CounterDatabase: RoomDatabase() {
    abstract fun counterDao(): CounterDAO
}