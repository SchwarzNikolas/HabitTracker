package com.habittracker.rootreflect.history

import androidx.compose.runtime.mutableStateListOf
import java.time.LocalDate
import java.time.Month

data class HistoryState(
    // stores all the months which recorded moods
    val monthsWithRecord: MutableList<Int> = mutableStateListOf(),
    // stores each day of selected month and its mood
    val dayList: MutableList<DayOfMonth> = mutableStateListOf(),
    // day which the user selects when clicking on a calendar day
    val selectedDate: LocalDate? = null,
    // month which the user selects when selecting it in the ListPicker
    val selectedMonth: Month = LocalDate.now().month,
    // TODO: Change year to be selectable
    val selectedYear: Int = LocalDate.now().year,
    // state of the bottom sheet (if it is up or hidden)
    val bottomSheetActive: Boolean = false,
    // the users mood on the selected day
    val selectedMood: String = "",
    // Colour variable for days without a logged mood
    val dayPassiveColour: Long = 0xFF000000
)