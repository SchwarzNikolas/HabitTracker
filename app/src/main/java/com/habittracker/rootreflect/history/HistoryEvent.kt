package com.habittracker.rootreflect.history

import java.time.Month

sealed interface HistoryEvent {
    data object GetCurrentMonth: HistoryEvent
    data class ChangeCurrentMonth(val month: Month) : HistoryEvent
}