package com.example.habittracker

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class DisplayHabit (
    val habit: MutableState<Habit>,

    val done: MutableState<Boolean> = mutableStateOf(false),

    val completion: MutableList<MutableState<Boolean>> = MutableList(size = habit.value.frequency){ mutableStateOf(false) }

) {
    operator fun contains(x: Habit?): Boolean {
        return x?.habitId == habit.value.habitId
    }
}