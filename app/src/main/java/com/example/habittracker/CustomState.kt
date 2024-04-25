package com.example.habittracker

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class CustomState(

    // Indicate if a user is in Custom screen or not
    val customMode: Boolean = true,
    // Bool to indicate if it's weekly or daily habit by the switch
    val isDaily: Boolean = true,
    // Habit name that will be displayed by the edit box
    val habitName: String = "badminton",
    // Frequency that will be displayed by the edit box
    val habitFrequency: String = "2",
    // Represent days for weekly habits
    val completion: MutableList<MutableState<Boolean>> = MutableList(size = 7){ mutableStateOf(true) },
)
