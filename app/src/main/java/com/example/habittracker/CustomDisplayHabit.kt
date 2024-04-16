package com.example.habittracker

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

// A lot of copy paste from DisplayHabit
data class CustomDisplayHabit(
    // Habit information from the database
    val cusHabit: MutableState<CustomHabit>,
    // bool of habit completion
    val done: MutableState<Boolean> = mutableStateOf(false),
    // state of the check boxes (list of booleans)
    val date: List<MutableState<String?>> =
        MutableList(size = 7){mutableStateOf(null)},
    val completion: List<MutableState<Boolean>> =
        MutableList(size = 7){mutableStateOf(false)}
) {
    // check if this display habit contains a habit
    // if display habit is a box, this checks if a specific habit is inside this box
    operator fun contains(x: Habit?): Boolean {
        return x?.habitId == cusHabit.value.id
    }
}