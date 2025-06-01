package com.example.habittracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HealthLogDao {
    @Insert
    suspend fun insertLog(log: HealthLog)

    @Query("SELECT * FROM health_logs WHERE userId = :userId ORDER BY date DESC")
    suspend fun getLogsForUser(userId: Int): List<HealthLog>
}

