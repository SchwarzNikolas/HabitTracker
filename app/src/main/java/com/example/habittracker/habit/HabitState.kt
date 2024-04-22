package com.example.habittracker.habit


import androidx.compose.runtime.mutableStateListOf
import com.example.habittracker.database.Habit
import com.example.habittracker.database.HabitJoin
import com.example.habittracker.database.HabitRecord


// This contains all the data that the UI displays
data class HabitState(
    val name: String = "test",
    // list of all the habits that will be displayed
    val habits: MutableList<DisplayHabit> = mutableStateListOf(),
    // contains what habits were completed on which dates
    val habitRecord: MutableList<HabitRecord> = mutableStateListOf(),
    // string that will be displayed by the edit window
    val editString: String = "",
    // int that will be displayed by the edit window
    val editFreq: String = "",
    // habit that is currently being edited
    val editHabit: Habit = Habit(),
    // bool of whether or not the edit window is being displayed
    val showEdit: Boolean = false,
    val habitJoin: MutableList<HabitJoin> = mutableListOf()
)