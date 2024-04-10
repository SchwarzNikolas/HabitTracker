package com.example.habittracker

import androidx.compose.runtime.mutableStateListOf

data class HabitState (
    val name: String = "test",
    val habits: MutableList<DisplayHabit> = mutableStateListOf()
)