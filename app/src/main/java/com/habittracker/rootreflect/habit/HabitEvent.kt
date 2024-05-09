package com.habittracker.rootreflect.habit

import com.habittracker.rootreflect.database.HabitJoin
import com.habittracker.rootreflect.mood.MoodType


// defines events that can be triggered by the UI
sealed interface HabitEvent {
    // data class can take arguments, data object cannot
    // each is explained in HabitViewModel
    data class MoodChange(val moodType: MoodType): HabitEvent
    data class IncCompletion(val habitJoin: HabitJoin): HabitEvent
    data class DecCompletion(val habitJoin: HabitJoin): HabitEvent
    data class IncFrequency(val frequency: Int): HabitEvent
    data class DecFrequency(val frequency: Int): HabitEvent
    data class EditHabit(val displayHabit: DisplayHabit): HabitEvent
    data class UpDateEditString(val newString : String): HabitEvent
    data class UpDateEditFreq(val newFreq : Int): HabitEvent
    data class UpDateEditDays(val newDayIndex : Int, val clicked: Boolean): HabitEvent
    data class ModifyHabit(val joinHabit: HabitJoin) : HabitEvent
    data class CancelEdit(val displayHabit: DisplayHabit) : HabitEvent
    data class DeleteHabit(val habitJoin: HabitJoin): HabitEvent
    data class ContextMenuVisibility(val displayHabit: DisplayHabit): HabitEvent
    // Deprecated
    data object ResetCompletion: HabitEvent
    data object NextDay: HabitEvent

}