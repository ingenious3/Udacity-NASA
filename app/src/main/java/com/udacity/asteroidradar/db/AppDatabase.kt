package com.udacity.asteroidradar.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.constant.Constants

@Database(entities = [AppDbEntity::class], version = Constants.DATABASE_VERSION)
abstract class AppDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var _DB_INSTANCE: AppDatabase

fun getDatabase(context: Context): AppDatabase {
    if (!::_DB_INSTANCE.isInitialized) {
        _DB_INSTANCE = Room.databaseBuilder(context.applicationContext,
            AppDatabase::class.java,
            Constants.DATABASE_NAME)
            .build()
    }
    return _DB_INSTANCE
}