package com.example.habittracker

sealed interface CustomHabitEvent {
    //User actions

    // Event to toggle the switch
    data object UpdateDaily: CustomHabitEvent
    // Edit Name
    data class EditName(val name: String): CustomHabitEvent
    // Edit frequency
    data class EditFreq(val frequency: String): CustomHabitEvent
    // CancelEdit
    data object CancelEdit: CustomHabitEvent
    // SaveEdit
    data object SaveEdit: CustomHabitEvent

}