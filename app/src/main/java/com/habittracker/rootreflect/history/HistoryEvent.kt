package com.habittracker.rootreflect.history

import com.habittracker.rootreflect.mood.MoodType
import java.time.Month

sealed interface HistoryEvent {
    data object EnableBottomSheet: HistoryEvent
    data object DisableBottomSheet: HistoryEvent
    data class ChangeCurrentMonth(val month: Month): HistoryEvent

    data class ChangeSelectedMood(val moodType: MoodType): HistoryEvent

}