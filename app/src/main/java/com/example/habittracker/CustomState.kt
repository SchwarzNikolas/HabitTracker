package com.example.habittracker

import androidx.compose.runtime.mutableStateListOf

data class CustomState(

    // Indicate if a user is in Custom screen or not
    val customMode: Boolean = true,
    // Name of the Custom screen
    val name: String = "Custom",
    // Bool to indicate if it's weekly or daily habit by the switch
    val isWeekly: Boolean = false,
    // Habit name that will be displayed by the edit box
    val habitName: String = "badminton",
    // Frequency that will be displayed by the edit box
    val habitFrequency: Int = 2,
    // A list to store the custom habits, which will not be displayed by the screen in Custom
    val weeklyHabitList: MutableList<DisplayHabit> = mutableStateListOf(),
)
