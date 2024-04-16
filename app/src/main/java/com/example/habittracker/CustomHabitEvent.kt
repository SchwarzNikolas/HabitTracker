package com.example.habittracker

sealed interface CustomHabitEvent {
    //User actions

    // ShowDialog
    data object ShowDialog: CustomHabitEvent
    // HideDialog
    data object HideDialog: CustomHabitEvent
    // EditName
    data class EditName(val name: String): CustomHabitEvent
    // EditFrequency
    data class EditFreq(val frequency: Int): CustomHabitEvent
    // BoxChecked
    data class BoxChecked(val cusDisplayHabit: CustomDisplayHabit, val freq: Int): CustomHabitEvent
    // CancelEdit
    data object CancelEdit: CustomHabitEvent
    // SaveEdit
    data class SaveEdit(val customDisplayHabit: CustomDisplayHabit): CustomHabitEvent
    // DeleteHabit
    data class DeleteCusHabit(val customDisplayHabit: CustomDisplayHabit): CustomHabitEvent

}