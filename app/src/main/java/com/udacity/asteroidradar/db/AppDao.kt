package com.udacity.asteroidradar.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {
    @Query("Select * From ${AppDbEntity.TABLE_NAME} WHERE closeApproachDate >= :today ORDER BY closeApproachDate ASC")
    fun getAsteroids(today:  String): LiveData<List<AppDbEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: List<AppDbEntity>)
}