package com.example.habittracker

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.habittracker.ui.theme.HabitTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ask for POST_NOTIFICATIONS permission if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        setContent {
            HabitTrackerTheme {
                var isLoggedIn by remember { mutableStateOf(false) }
                var showRegister by remember { mutableStateOf(false) }

                Surface(modifier = Modifier.fillMaxSize()) {
                    when {
                        showRegister -> RegistrationScreen(onBack = { showRegister = false })
                        isLoggedIn -> HomeScreen()
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
fun LoginScreen(onLogin: () -> Unit, onRegisterClick: () -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to Habit Tracker!",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF4169E1)
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = "",
            onValueChange = {},
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = "",
            onValueChange = {},
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
            onLogin()
        }) {
            Text(text = "Log In")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Don't have an account? Register here",
            color = Color(0xFF800080),
            modifier = Modifier.clickable { onRegisterClick() }
        )
    }
}

@Composable
fun RegistrationScreen(onBack: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
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
                Toast.makeText(context, "Registered Successfully!", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Register")
        }

        Text(
            text = "Back to Login",
            color = Color.Blue,
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable { onBack() }
        )
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

@Composable
fun HomeScreen() {
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
    }
}

fun isValidEmail(email: String): Boolean {
    val regex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
    return regex.matches(email)
}

