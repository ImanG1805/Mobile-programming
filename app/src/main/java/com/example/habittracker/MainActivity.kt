package com.example.habittracker

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.habittracker.ui.theme.HabitTrackerTheme
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        setContent {
            HabitTrackerTheme {
                var isLoggedIn by remember { mutableStateOf(false) }
                var showRegister by remember { mutableStateOf(false) }
                var showCreateHabit by remember { mutableStateOf(false) }
                var showHabits by remember { mutableStateOf(false) }
                var showHealthLog by remember { mutableStateOf(false) }


                Surface(modifier = Modifier.fillMaxSize()) {
                    when {
                        showHealthLog -> HealthLogScreen(
                            userId = 1,
                            onBack = { showHealthLog = false }
                        )
                        showHabits -> HabitListScreen(userId = 1, onBack = { showHabits = false })
                        showRegister -> RegistrationScreen(onBack = { showRegister = false })
                        isLoggedIn && showCreateHabit -> CreateCustomHabitScreen(onSave = { showCreateHabit = false })
                        isLoggedIn -> HomeScreen(
                            onCreateHabit = { showCreateHabit = true },
                            onViewHabits = { showHabits = true },
                            onOpenHealthLog = { showHealthLog = true }
                        )
                        else -> LoginScreen(
                            onLogin = { isLoggedIn = true },
                            onRegisterClick = { showRegister = true }
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun LoginScreen(onLogin: () -> Unit, onRegisterClick: () -> Unit, viewModel: LoginViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginSuccess = viewModel.loginSuccess
    LaunchedEffect(loginSuccess) {
        if (loginSuccess == true) {
            Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
            onLogin()
        } else if (loginSuccess == false) {
            Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to Habit Tracker!", style = MaterialTheme.typography.headlineMedium, color = Color(0xFF4169E1))
        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = username, onValueChange = { username = it }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(context, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(username, password)
            }
        }) {
            Text("Log In")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Don't have an account? Register here", color = Color(0xFF800080), modifier = Modifier.clickable { onRegisterClick() })
    }
}

@Composable
fun RegistrationScreen(onBack: () -> Unit, viewModel: RegisterViewModel = hiltViewModel()) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val success by viewModel.registrationSuccess.collectAsState()

    LaunchedEffect(success) {
        if (success) {
            Toast.makeText(context, "Registered successfully!", Toast.LENGTH_SHORT).show()
            onBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Register", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        CustomInputField(value = username, label = "Username") { username = it }
        Spacer(modifier = Modifier.height(8.dp))
        CustomInputField(value = email, label = "Email") { email = it }
        Spacer(modifier = Modifier.height(8.dp))
        CustomInputField(value = password, label = "Password", isPassword = true) { password = it }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (!isValidEmail(email)) {
                Toast.makeText(context, "Invalid Email", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.registerUser(username, email, password)
            }
        }) {
            Text("Register")
        }

        Text("Back to Login", color = Color.Blue, modifier = Modifier.padding(top = 16.dp).clickable { onBack() })
    }
}

@Composable
fun HomeScreen(onCreateHabit: () -> Unit, onViewHabits: () -> Unit,onOpenHealthLog: () -> Unit) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val intent = Intent(context, HabitReminderService::class.java)
        ContextCompat.startForegroundService(context, intent)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to your Habit Dashboard!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onCreateHabit) {
            Text("Create Custom Habit")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onViewHabits) {
            Text("See Your Habits")
        }
        Button(onClick = onOpenHealthLog) {
            Text("Open Health Log")
        }

    }
}

@Composable
fun CustomInputField(
    value: String,
    label: String,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val visual = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = visual,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            if (isPassword) {
                val icon = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                val desc = if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = desc)
                }
            }
        }
    )
}

fun isValidEmail(email: String): Boolean {
    val regex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
    return regex.matches(email)
}
