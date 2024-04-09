package com.example.habittracker

data class HabitState (
    val name: String = "test",
    val habits: List<DisplayHabit> = listOf<DisplayHabit>()
)