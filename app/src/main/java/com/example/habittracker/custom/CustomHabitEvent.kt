package com.example.habittracker.custom

sealed interface CustomHabitEvent {
    //User actions

    // Event to toggle the switch
    data object UpdateDaily: CustomHabitEvent
    // Edit Name
    data class EditName(val name: String): CustomHabitEvent
    // Edit frequency
    data class EditFreq(val frequency: String): CustomHabitEvent
    data class ToggleDay(val dayIndex: Int): CustomHabitEvent
    // SaveEdit
    data object SaveEdit: CustomHabitEvent

}