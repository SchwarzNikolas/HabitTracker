package com.habittracker.rootreflect.custom

sealed interface CustomHabitEvent {
    //User actions

    // Event to toggle the switch
    data object UpdateDaily: CustomHabitEvent
    // Edit Name
    data class EditName(val name: String): CustomHabitEvent
    // Edit frequency
    data class EditFreq(val frequency: Int): CustomHabitEvent
    data class ToggleDay(val dayIndex: Int): CustomHabitEvent
    // SaveEdit
    data object SaveEdit: CustomHabitEvent
    // change keyboard focus event
    data class KeyboardFocus(val isFocused: Boolean): CustomHabitEvent
    // show notification after trying to save a habit
    data object ToggleNotificationVisibility : CustomHabitEvent

}