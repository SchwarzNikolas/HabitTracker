package com.habittracker.rootreflect.history

import java.time.LocalDate

data class DayOfMonth(
    /*
    Stores information of each day in a month
    Will be displayed in the calendar and bottom sheet
     */
    val mood: String,
    val colour: Long = 0xFF000000,
    val date: LocalDate,
)