package com.example.habittracker.data
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "users")
data class User(
 @PrimaryKey val username: String,
 val email: String,
 val password: String
)


