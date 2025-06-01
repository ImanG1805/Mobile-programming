package com.example.habittracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.habittracker.UserDao


@Database(entities = [User::class, Habit::class, Progress::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun progressDao(): ProgressDao
    abstract fun userDao(): UserDao
}


