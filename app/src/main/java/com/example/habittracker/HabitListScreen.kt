package com.example.habittracker

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habittracker.data.Habit

@Composable
fun HabitListScreen(userId: Int, onBack: () -> Unit, viewModel: HabitViewModel = hiltViewModel()) {
    val habits by viewModel.habits.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadHabitsForUser(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Your Habits", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (habits.isEmpty()) {
            Text("No habits found.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn {
                items(habits) { habit ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Name: ${habit.name}", style = MaterialTheme.typography.bodyLarge)
                            Text("Frequency: ${habit.frequency}")
                            habit.reminderTime?.let {
                                Text("Reminder: $it")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}
