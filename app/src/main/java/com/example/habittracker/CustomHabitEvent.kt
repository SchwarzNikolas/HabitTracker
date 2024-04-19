package com.example.habittracker

import com.example.habittracker.habit.DisplayHabit

sealed interface CustomHabitEvent {
    //User actions

    // Event to toggle the switch
    data object UpdateDaily: CustomHabitEvent
    // Edit Name
    data class EditName(val name: String): CustomHabitEvent
    // Edit frequency
    data class EditFreq(val frequency: Int): CustomHabitEvent
    // CancelEdit
    data object CancelEdit: CustomHabitEvent
    // SaveEdit
    data class SaveEdit(val displayHabit: DisplayHabit): CustomHabitEvent

}