package com.example.habittracker


import androidx.compose.runtime.mutableStateListOf



// This contains all the data that the UI displays
data class HabitState(
    val name: String = "test",
    val habits: MutableList<DisplayHabit> = mutableStateListOf(),
    val habitRecord: MutableList<HabitRecord> = mutableStateListOf(),
    val editString: String = "",
    val editFreq: Int = 0,
    val editHabit: Habit = Habit(),
    val showEdit: Boolean = false
)