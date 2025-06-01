package com.example.habittracker
import androidx.room.*
import com.example.habittracker.data.User

@Dao
interface UserDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE) suspend fun insertUser(user: User)
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): User?
}
