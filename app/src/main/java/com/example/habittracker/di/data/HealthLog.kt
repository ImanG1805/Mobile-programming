package com.example.habittracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_logs")
data class HealthLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val date: String,
    val waterIntake: Int,
    val proteinIntake: Int,
    val workoutNotes: String

)


