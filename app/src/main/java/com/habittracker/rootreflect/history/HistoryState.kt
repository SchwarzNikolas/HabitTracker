package com.habittracker.rootreflect.history

import java.sql.Date
import java.time.LocalDate
import java.time.Month

data class HistoryState(
    val selectedDate: Date = Date.valueOf("1970-01-01"),
    val selectedMonth: Month = LocalDate.now().month,
)