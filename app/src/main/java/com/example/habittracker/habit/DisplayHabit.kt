package com.example.habittracker.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.habittracker.database.Habit
import com.example.habittracker.database.HabitCompletion
import com.example.habittracker.database.HabitJoin

// associates each habit with additional information needed for UI
data class DisplayHabit (

    val isMenuVisible: MutableState<Boolean> = mutableStateOf(false),

    val beingEdited : MutableState<Boolean> = mutableStateOf(false),

    val habitJoin: HabitJoin = HabitJoin(Habit(), HabitCompletion())

) {

}