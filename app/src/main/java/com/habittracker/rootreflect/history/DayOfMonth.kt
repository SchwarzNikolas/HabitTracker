package com.habittracker.rootreflect.history

import androidx.compose.ui.graphics.Color
import com.habittracker.rootreflect.ui.theme.ForestShadow
import java.time.LocalDate

data class DayOfMonth(
    /*
    Stores information of each day in a month
    Will be displayed in the calendar and bottom sheet
     */
    val mood: String,
    val colour: Color = ForestShadow,
    val date: LocalDate,
)