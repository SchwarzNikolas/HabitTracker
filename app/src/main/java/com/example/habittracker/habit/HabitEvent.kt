package com.example.habittracker.habit

import com.example.habittracker.database.Habit
import com.example.habittracker.database.HabitJoin


// defines events that can be triggered by the UI
sealed interface HabitEvent {
    // data class can take arguments, data object cannot
    // each is explained in HabitViewModel

    data class IncCompletion(val habitJoin: HabitJoin): HabitEvent
    data class DecCompletion(val habitJoin: HabitJoin): HabitEvent
    data class EditHabit(val habit: Habit): HabitEvent
    data class UpDateEditString(val newString : String): HabitEvent
    data class UpDateEditFreq(val newFreq : Int): HabitEvent
    data object ModifyHabit: HabitEvent
    data object CancelEdit: HabitEvent
    data class DeleteHabit(val habitJoin: HabitJoin): HabitEvent
    data class ContextMenuVisibility(val displayHabit: DisplayHabit): HabitEvent

    // Deprecated
    data class BoxChecked(val displayHabit: DisplayHabit, val index:Int): HabitEvent
    // Deprecated
    data object resetCompletion: HabitEvent
}