package com.habittracker.rootreflect.history


import androidx.compose.ui.unit.DpOffset
import com.habittracker.rootreflect.database.HabitRecord
import java.time.LocalDate

sealed interface HistoryEvent {
    // event to enable bottom sheet
    data object ShowSummary: HistoryEvent
    // event to disable bottom sheet
    data object DisableBottomSheet: HistoryEvent
    // event to change selected month
    data class ChangeCurrentMonth(val date: Int): HistoryEvent
    // event to change selected day
    data class ChangeSelectedDay(val day: LocalDate, val moodName: String): HistoryEvent
    // event to select a plant
    data class SelectPlant(val recordedHabit: HabitRecord): HistoryEvent

    data object NameTagToggle: HistoryEvent

    data class SetOffSet(val offSet: DpOffset, val habitRecord: String): HistoryEvent

}

