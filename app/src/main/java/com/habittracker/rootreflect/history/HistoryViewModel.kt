package com.habittracker.rootreflect.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habittracker.rootreflect.database.HabitDao
import com.habittracker.rootreflect.database.MoodRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate
import java.time.YearMonth

class HistoryViewModel(
      private val dao: HabitDao
): ViewModel() {
    private val _state = MutableStateFlow(HistoryState())
    val state = _state

    init {
        val year: YearMonth = YearMonth.of(LocalDate.now().year, state.value.selectedMonth)
        val amountDays: Int = year.lengthOfMonth()
        val days: MutableList<DayOfMonth> = mutableListOf()
        viewModelScope.launch {
            for (i in 1..amountDays) {
                // Change Mood entity to date instead of string and delete all .toStrings()
                val date = LocalDate.of(
                    state.value.selectedYear,
                    state.value.selectedMonth.ordinal + 1,
                    i
                )
                val colour = dao.getMoodRecByDate(date.toString())?.mood?.moodColor
                    ?: state.value.dayPassiveColour
                days.add(
                    DayOfMonth(
                        colour = colour,
                        date = Date.valueOf(date.toString())
                    )
                )
            }
            _state.update {
                it.copy(
                    dayList = days
                )
            }
        }
    }

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.EnableBottomSheet -> {
                _state.update {
                    it.copy(
                        bottomSheetActive = true
                    )
                }
            }
            is HistoryEvent.DisableBottomSheet -> {
                _state.update {
                    it.copy(
                        bottomSheetActive = false
                    )
                }
            }
//            is HistoryEvent.GetCurrentMonth -> {
//                _state.update {
//                    it.copy(
//                        selectedMonth = LocalDate.now().month
//                    )
//                }
//            }
            is HistoryEvent.ChangeCurrentMonth -> {
                _state.update {
                    it.copy(
                        selectedMonth = event.month
                    )
                }
                // change to selected year instead of current year
                val year: YearMonth = YearMonth.of(LocalDate.now().year, state.value.selectedMonth)
                val amountDays: Int = year.lengthOfMonth()
                val days: MutableList<DayOfMonth> = mutableListOf()
                viewModelScope.launch {
                    for (i in 1..amountDays) {
                        // Change Mood entity to date instead of string and delete all .toStrings()
                            val date = LocalDate.of(
                                state.value.selectedYear,
                                state.value.selectedMonth.ordinal + 1,
                                i
                            )
                            val colour = dao.getMoodRecByDate(date.toString())?.mood?.moodColor
                                ?: state.value.dayPassiveColour
                            days.add(
                                DayOfMonth(
                                    colour = colour,
                                    date = Date.valueOf(date.toString())
                                )
                            )
                    }
                    _state.update {
                        it.copy(
                            dayList = days
                        )
                    }
                }
            }
            is HistoryEvent.ChangeSelectedMood -> {
                _state.update {
                    it.copy(
                        selectedMood = event.moodType
                    )
                }
            }
        }
    }
}