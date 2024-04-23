package com.example.habittracker.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.habittracker.database.Habit
import com.example.habittracker.database.HabitCompletion
import com.example.habittracker.database.HabitJoin

// associates each habit with additional information needed for UI
data class DisplayHabit (
    // Deprecated
    // habit information from the database
    val habit: MutableState<Habit> = mutableStateOf(Habit()),
    // Deprecated
    // bool of habit completion
    val done: MutableState<Boolean> = mutableStateOf(false),
    // Deprecated
    // state of the check boxes (list of bools)
    val completion: MutableList<MutableState<Boolean>> = MutableList(size = habit.value.frequency){ mutableStateOf(false) },

    val isMenuVisible: MutableState<Boolean> = mutableStateOf(false),

    val beingEdited : MutableState<Boolean> = mutableStateOf(false),

    val habitJoin: HabitJoin = HabitJoin(Habit(), HabitCompletion())

) {
    // Deprecated
    // check if this display habit contains a habit
    // if display habit is a box, this checks if a specific habit is inside this box
    operator fun contains(x: Habit?): Boolean {
        return x?.habitId == habit.value.habitId
    }
}