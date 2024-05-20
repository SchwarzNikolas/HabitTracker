package com.habittracker.rootreflect.habit

import com.habittracker.rootreflect.database.Habit


// defines events that can be triggered by the UI
sealed interface HabitEvent {
    // data class can take arguments, data object cannot
    // each is explained in HabitViewModel

    data class IncCompletion(val habit: Habit): HabitEvent
    data class DecCompletion(val habit: Habit): HabitEvent
    data class EditHabit(val displayHabit: DisplayHabit): HabitEvent
    data class UpDateEditString(val newString : String): HabitEvent
    data class UpDateEditFreq(val newFreq : Int): HabitEvent
    data class UpDateEditDays(val newDayIndex : Int, val clicked: Boolean): HabitEvent
    data class ModifyHabit(val displayHabit: DisplayHabit) : HabitEvent
    data class CancelEdit(val displayHabit: DisplayHabit) : HabitEvent
    data class DeleteHabit(val habit: Habit): HabitEvent
    data class ContextMenuVisibility(val displayHabit: DisplayHabit): HabitEvent

    data class MoodSelected(val moodType: MoodType): HabitEvent

    data class CheckCompletion (val habit: Habit):HabitEvent
    data object ToggleNotificationVisibility: HabitEvent

    data object ToggleScroll: HabitEvent
}