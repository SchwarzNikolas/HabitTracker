package com.habittracker.rootreflect.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.habittracker.rootreflect.database.Habit
import com.habittracker.rootreflect.database.HabitCompletion
import com.habittracker.rootreflect.database.HabitJoin

// associates each habit with additional information needed for UI
data class DisplayHabit (

    val isMenuVisible: MutableState<Boolean> = mutableStateOf(false),

    val beingEdited : MutableState<Boolean> = mutableStateOf(false),

    val habitJoin: HabitJoin = HabitJoin(Habit(), HabitCompletion())

) {

}