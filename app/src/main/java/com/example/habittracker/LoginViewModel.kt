package com.example.habittracker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {

    var loginSuccess by mutableStateOf<Boolean?>(null)
        private set

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = userDao.login(username, password)
            loginSuccess = user != null


            if (loginSuccess == true) {
                Log.d("DB_DEBUG", "Login successful for: $username")
            } else {
                Log.d("DB_DEBUG", "Login failed for: $username")
            }
        }
    }
}
