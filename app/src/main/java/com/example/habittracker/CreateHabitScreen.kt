package com.example.habittracker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateCustomHabitScreen(onSave: () -> Unit) {
    var habitName by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("Daily") }
    var reminderEnabled by remember { mutableStateOf(false) }
    var reminderTime by remember { mutableStateOf("08:00") }

    val frequencyOptions = listOf("Daily", "Weekly", "Custom")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create a New Habit", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = habitName,
            onValueChange = { habitName = it },
            label = { Text("Habit Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Frequency", style = MaterialTheme.typography.labelLarge)
        frequencyOptions.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { frequency = option }
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(selected = frequency == option, onClick = { frequency = option })
                Text(option)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = reminderEnabled,
                onCheckedChange = { reminderEnabled = it }
            )
            Text("Enable Daily Reminder")
        }

        if (reminderEnabled) {
            OutlinedTextField(
                value = reminderTime,
                onValueChange = { reminderTime = it },
                label = { Text("Reminder Time (HH:mm)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // You can save the habit to a list or database here
                onSave()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Habit")
        }
    }
}