package com.example.habittracker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.HealthLog
import com.example.habittracker.data.HealthLogDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthLogViewModel @Inject constructor(
    private val healthLogDao: HealthLogDao
) : ViewModel() {

    private val _logs = MutableStateFlow<List<HealthLog>>(emptyList())
    val logs: StateFlow<List<HealthLog>> = _logs

    fun addLog(log: HealthLog) {
        viewModelScope.launch {
            healthLogDao.insertLog(log)
            Log.d("DB_DEBUG", "Inserted log: $log")
            loadLogs(log.userId)
        }
    }

    fun loadLogs(userId: Int) {
        viewModelScope.launch {
            try {
                val userLogs = healthLogDao.getLogsForUser(userId)
                _logs.value = userLogs
                Log.d("DB_DEBUG", "Loaded ${userLogs.size} logs for user $userId")
            } catch (e: Exception) {
                Log.e("DB_DEBUG", "Failed to load logs: ${e.message}")
            }
        }
    }
}
