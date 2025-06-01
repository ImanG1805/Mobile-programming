package com.example.habittracker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habittracker.data.HealthLog


@Composable
fun HealthLogScreen(
    userId: Int,
    viewModel: HealthLogViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val logs by viewModel.logs.collectAsState()

    var water by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var workout by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadLogs(userId)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Track Daily Health", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = water,
            onValueChange = { water = it },
            label = { Text("Water Intake (ml)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = protein,
            onValueChange = { protein = it },
            label = { Text("Protein Intake (g)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = workout,
            onValueChange = { workout = it },
            label = { Text("Workout Notes") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val log = HealthLog(
                    userId = userId,
                    date = getCurrentDate(),
                    waterIntake = water.toIntOrNull() ?: 0,
                    proteinIntake = protein.toIntOrNull() ?: 0,
                    workoutNotes = workout
                )
                viewModel.addLog(log)
                water = ""
                protein = ""
                workout = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Log")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Past Logs", style = MaterialTheme.typography.titleMedium)

        LazyColumn {
            items(logs) { log ->
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    Text("Date: ${log.date}")
                    Text("Water: ${log.waterIntake} ml")
                    Text("Protein: ${log.proteinIntake} g")
                    Text("Workout: ${log.workoutNotes}")
                    Divider()
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }
}

