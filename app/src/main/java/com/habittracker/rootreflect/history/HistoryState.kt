package com.habittracker.rootreflect.history

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.habittracker.rootreflect.database.HabitRecord
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
    // the selected year of the displayed month
    val selectedYear: Int = LocalDate.now().year,
    // state of the bottom sheet (if it is up or hidden)
    val bottomSheetActive: Boolean = false,
    // the users mood on the selected day
    val selectedMood: String = "",
    // Colour variable for days without a logged mood
    val dayPassiveColour: Long = 0xFF000000,
    /*
     if the variable is false information about the day will be displayed in the bottom sheet
     otherwise habit information will be displayed
     */
    val habitInfo: Boolean = false,
    // list of habits of the selected day
    val habitList: MutableList<HabitRecord> = mutableStateListOf(),


    val nameTagActive: Boolean = false,

    val offset: DpOffset = DpOffset(0.dp,0.dp),
    val name  : String = "",

    // list for habits (frequency 1)
    val habitListF1: MutableList<HabitRecord> = mutableStateListOf(),
    // list for habits (frequency 2)
    val habitListF2: MutableList<HabitRecord> = mutableStateListOf(),
    // list for habits (frequency 3+)
    val habitListF3Above: MutableList<HabitRecord> = mutableStateListOf(),


)