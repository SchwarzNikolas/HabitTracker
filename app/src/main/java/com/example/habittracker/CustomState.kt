package com.example.habittracker

import androidx.compose.runtime.mutableStateListOf

data class CustomState(

    // Indicate if a user is in Custom screen or not
    val customMode: Boolean = true,
    // Bool to indicate if it's weekly or daily habit by the switch
    val isDaily: Boolean = true,
    // Habit name that will be displayed by the edit box
    val habitName: String = "badminton",
    // Frequency that will be displayed by the edit box
    val habitFrequency: Int = 2,
)
