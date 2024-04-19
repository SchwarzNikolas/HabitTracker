package com.example.habittracker

sealed interface CustomHabitEvent {
    //User actions

    //switch to weekly habit
    data class switchToWeekly(val isWeekly: Boolean = true): CustomHabitEvent
    //switch to daily habit
    data class switchToDaily(val isWeekly: Boolean = false): CustomHabitEvent
    // Edit Name
    data class EditName(val name: String): CustomHabitEvent
    // Edit frequency
    data class EditFreq(val frequency: Int): CustomHabitEvent
    // CancelEdit
    data object CancelEdit: CustomHabitEvent
    // SaveEdit
    data class SaveEdit(val displayHabit: DisplayHabit): CustomHabitEvent

}