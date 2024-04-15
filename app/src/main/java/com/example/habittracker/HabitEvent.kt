package com.example.habittracker

import androidx.compose.runtime.MutableState


// defines events that can be triggered by the UI
sealed interface HabitEvent {
    data class ModifyHabit(val habit: DisplayHabit, val frequency: Int): HabitEvent
    data class RecordHabitCompletion(val habit: DisplayHabit): HabitEvent
    data class BoxChecked(val displayHabit: DisplayHabit, val index: Int): HabitEvent
}