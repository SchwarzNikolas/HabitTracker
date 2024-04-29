package com.example.habittracker.habit

import com.example.habittracker.database.HabitJoin


// defines events that can be triggered by the UI
sealed interface HabitEvent {
    // data class can take arguments, data object cannot
    // each is explained in HabitViewModel

    data class IncCompletion(val habitJoin: HabitJoin): HabitEvent
    data class DecCompletion(val habitJoin: HabitJoin): HabitEvent
    data class EditHabit(val displayHabit: DisplayHabit): HabitEvent
    data class UpDateEditString(val newString : String): HabitEvent
    data class UpDateEditFreq(val newFreq : Int): HabitEvent
    data class UpDateEditDays(val newDayIndex : Int, val clicked: Boolean): HabitEvent
    data class ModifyHabit(val joinHabit: HabitJoin) : HabitEvent
    data class CancelEdit(val displayHabit: DisplayHabit) : HabitEvent
    data class DeleteHabit(val habitJoin: HabitJoin): HabitEvent
    data class ContextMenuVisibility(val displayHabit: DisplayHabit): HabitEvent

    // Deprecated
    data class BoxChecked(val displayHabit: DisplayHabit, val index:Int): HabitEvent
    // Deprecated
    data object ResetCompletion: HabitEvent
    data object NextDay: HabitEvent

}