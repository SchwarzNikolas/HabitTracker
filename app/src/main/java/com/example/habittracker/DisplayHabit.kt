package com.example.habittracker

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

// associates each habit with additional information needed for UI
data class DisplayHabit (
    // habit information from the database
    val habit: MutableState<Habit> = mutableStateOf(Habit()),
    // bool of habit completion
    val done: MutableState<Boolean> = mutableStateOf(false),
    // state of the check boxes (list of bools)
    val completion: MutableList<MutableState<Boolean>> = MutableList(size = habit.value.frequency){ mutableStateOf(false) }

) {
    // check if this display habit contains a habit
    // if display habit is a box, this checks if a specific habit is inside this box
    operator fun contains(x: Habit?): Boolean {
        return x?.habitId == habit.value.habitId
    }
}