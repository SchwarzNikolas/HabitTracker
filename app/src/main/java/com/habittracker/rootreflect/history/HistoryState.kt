package com.habittracker.rootreflect.history

import androidx.compose.runtime.mutableStateListOf
import com.habittracker.rootreflect.database.MoodRecord
import com.habittracker.rootreflect.mood.MoodType
import java.sql.Date
import java.time.LocalDate
import java.time.Month

data class HistoryState(
    val habitRecord: MutableList<MoodRecord> = mutableStateListOf(),
    val dayList: MutableList<DayOfMonth> = mutableStateListOf(),
    val selectedDate: Date? = null,
    val selectedMonth: Month = LocalDate.now().month,
    val bottomSheetActive: Boolean = false,
    val selectedMood: MoodType = MoodType.GOOD
)