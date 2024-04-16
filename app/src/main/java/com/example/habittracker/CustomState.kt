package com.example.habittracker

import androidx.compose.runtime.mutableStateListOf

data class CustomState(

    val name: String = "testCustomState",
    // list of all the habits that will be displayed
    val cusHabits: MutableList<CustomDisplayHabit> = mutableStateListOf(),
    // contains what habits were completed on which dates
    // val habitRecord: MutableList<HabitRecord> = mutableStateListOf(),
    // string that will be displayed by the edit window
    val editString: String = "",
    // int that will be displayed by the edit window
    val editFreq: Int = 0,
    // habit that is currently being edited
    val editCusHabit: CustomHabit = CustomHabit(),
    // bool of whether or not the edit window is being displayed
    val showEdit: Boolean = false
)
