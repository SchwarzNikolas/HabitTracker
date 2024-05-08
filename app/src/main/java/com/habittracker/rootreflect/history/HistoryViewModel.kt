package com.habittracker.rootreflect.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habittracker.rootreflect.database.HabitDao
import com.habittracker.rootreflect.database.HabitRecord
import com.habittracker.rootreflect.database.MoodRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

class HistoryViewModel(
      private val dao: HabitDao
): ViewModel() {
    private val _state = MutableStateFlow(HistoryState())
    val state = _state

    init {
        viewModelScope.launch {
            dao.fetchMoodRecords().collect{moodRecords -> run{
                val displayMoodRecordList: MutableList<MoodRecord> = mutableListOf()
                for (record in moodRecords){
                    displayMoodRecordList.add(record)
                }
                _state.update { it.copy(
                    habitRecord = displayMoodRecordList
                )
                }
            }
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
                val year: YearMonth = YearMonth.of(LocalDate.now().year, state.value.selectedMonth)
                val amountDays: Int = year.lengthOfMonth()
                val days: MutableList<DayOfMonth> = mutableListOf()
                for (i in 1..amountDays) {
                    // Change to DATE!!!!
                    val date: String = LocalDate.now().year.toString()+"-"+(state.value.selectedMonth.ordinal+1).toString()+"-"+1.toString()
                    var colour: Long = 0xffffff
                    viewModelScope.launch {
                        colour = dao.getMoodRecByDate(date)?.mood?.moodColor ?: 0xffffff
                    }
                    days.add(DayOfMonth(
                        colour = colour,
                        date = Date.valueOf(date)
                    ))
                }
                _state.update {
                    it.copy(
                        dayList = days
                    )
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