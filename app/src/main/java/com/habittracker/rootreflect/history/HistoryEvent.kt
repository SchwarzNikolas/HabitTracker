package com.habittracker.rootreflect.history

import java.time.LocalDate
import java.time.Month

sealed interface HistoryEvent {
    // event to enable bottom sheet
    data object EnableBottomSheet: HistoryEvent
    // event to disable bottom sheet
    data object DisableBottomSheet: HistoryEvent
    // event to change selected month
    data class ChangeCurrentMonth(val date: Int): HistoryEvent
    // event to change selected day
    data class ChangeSelectedDay(val day: LocalDate, val moodName: String): HistoryEvent

}