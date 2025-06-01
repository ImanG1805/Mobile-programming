package com.example.habittracker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.Habit
import com.example.habittracker.data.HabitDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitDao: HabitDao
) : ViewModel() {
    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits


    fun loadHabitsForUser(userId: Int) {
        viewModelScope.launch {
            try {
                val habitsFromDb = habitDao.getHabitsForUser(userId)
                _habits.value = habitsFromDb
                Log.d("DB_DEBUG", "Loaded ${habitsFromDb.size} habits for user $userId")
            } catch (e: Exception) {
                Log.e("DB_DEBUG", "Error loading habits: ${e.message}")
            }
        }
    }


    fun insertHabit(habit: Habit) {
        viewModelScope.launch {
            try {
                habitDao.insert(habit)
                Log.d("DB_DEBUG", "Inserted habit: ${habit.name}")
                loadHabitsForUser(habit.userId)
            } catch (e: Exception) {
                Log.e("DB_DEBUG", "Error inserting habit: ${e.message}")
            }
        }
    }
}
