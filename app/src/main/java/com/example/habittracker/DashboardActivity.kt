package com.example.habittracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.habittracker.ui.theme.HabitTrackerTheme

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            HabitTrackerTheme {
                DashboardScreen()
            }
        }
    }
}

@Composable
fun DashboardScreen() {

    val context = LocalContext.current

    var water by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var workout by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = water,
            onValueChange = { water = it },
            label = { Text("Water intake (ml)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = protein,
            onValueChange = { protein = it },
            label = { Text("Protein intake (g)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = workout,
            onValueChange = { workout = it },
            label = { Text("Workout details") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            Toast.makeText(
                context,
                "Saved: Water = $water ml, Protein = $protein g, Workout = $workout",
                Toast.LENGTH_LONG
            ).show()
        }) {
            Text("Save")
        }
    }
}
