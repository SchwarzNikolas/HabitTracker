package com.habittracker.rootreflect.custom

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class CustomState(
    // Bool to indicate if it's weekly or daily habit by the switch
    val isDaily: Boolean = true,
    // Habit name that will be displayed by the edit box
    val habitName: String = "",
    // Frequency that will be displayed by the edit box
    val habitFrequency: Int = 1,
    // Represent days for weekly habits
    val completion: MutableList<MutableState<Boolean>> = MutableList(size = 7){ mutableStateOf(false) },
    // state for keyboard focus
    val keyboardFocus: Boolean = false,

    val notificationText: String = "",

    val notificationVisibility: Boolean = false
)
