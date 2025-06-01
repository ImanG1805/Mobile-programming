package com.example.habittracker

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
}
