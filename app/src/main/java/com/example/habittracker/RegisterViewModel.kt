package com.example.habittracker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.habittracker.data.User





@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> get() = _registrationSuccess

    fun registerUser (username: String, email: String, password: String) {
        viewModelScope.launch {
            val newUser = User(username = username, email = email, password = password)
            userDao.insertUser(newUser)
            Log.d("DB_DEBUG", "User inserted: $username, $email")
            _registrationSuccess.value = true
        }
    }
}



