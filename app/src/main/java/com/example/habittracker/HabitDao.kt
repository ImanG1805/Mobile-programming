

package com.example.habittracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HabitDao {
    @Insert
    suspend fun insert(habit: Habit)

    @Query("SELECT * FROM habits WHERE userId = :userId")
    suspend fun getHabitsForUser(userId: Int): List<Habit>
}


