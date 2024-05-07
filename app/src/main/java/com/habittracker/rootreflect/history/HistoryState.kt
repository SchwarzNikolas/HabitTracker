package com.habittracker.rootreflect.history

import java.sql.Date

data class HistoryState(
    val selectedDate: Date = Date.valueOf("1970-01-01")
)