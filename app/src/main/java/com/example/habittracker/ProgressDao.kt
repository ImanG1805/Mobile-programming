package com.example.habittracker.data

import androidx.room.*

@Dao
interface ProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: Progress)

    @Query("SELECT * FROM progress WHERE habitId = :habitId")
    suspend fun getProgressForHabit(habitId: Int): List<Progress>
}