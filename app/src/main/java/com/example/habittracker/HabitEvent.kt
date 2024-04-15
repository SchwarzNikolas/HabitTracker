package com.example.habittracker

import androidx.compose.runtime.MutableState


// defines events that can be triggered by the UI
sealed interface HabitEvent {
    data object ModifyHabit : HabitEvent
    data class BoxChecked(val displayHabit: DisplayHabit, val index:Int): HabitEvent
    data class EditHabit(val displayHabit: DisplayHabit): HabitEvent
    data class UpDateEditString(val newString : String): HabitEvent
    data class UpDateEditFreq(val newFreq : Int): HabitEvent
    data object CancelEdit: HabitEvent
    data class DeleteHabit(val displayHabit: DisplayHabit): HabitEvent
}