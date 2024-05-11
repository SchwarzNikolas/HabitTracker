package com.habittracker.rootreflect.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habittracker.rootreflect.database.HabitDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

class HistoryViewModel(
      private val dao: HabitDao
): ViewModel() {
    private val _state = MutableStateFlow(HistoryState())
    val state = _state

    init {
        // update the current month when app is launched
        updateDays()
    }

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.EnableBottomSheet -> {
                // enables the bottom sheet (is invoked when the user clicks on anything that contains additional information)
                _state.update {
                    it.copy(
                        bottomSheetActive = true
                    )
                }
            }
            is HistoryEvent.DisableBottomSheet -> {
                // disable the bottom sheet
                _state.update {
                    it.copy(
                        bottomSheetActive = false
                    )
                }
            }
            is HistoryEvent.ChangeCurrentMonth -> {
                // change selected month and update the calendar accordingly
                _state.update {
                    it.copy(
                        selectedMonth = event.month
                    )
                }
                updateDays()
            }
            is HistoryEvent.ChangeSelectedDay -> {
                // change selected date (is invoked when user selects a day in the calendar)
                _state.update {
                    it.copy(
                        selectedDate = event.day,
                        selectedMood = event.moodName
                    )
                }
            }
        }
    }

    private fun updateDays(){
        /*
        method which fetches all logged moods for a certain month and updates a list of days in the state
         */
        // change to selected year instead of current year
        val year: YearMonth = YearMonth.of(LocalDate.now().year, state.value.selectedMonth)
        val amountDays: Int = year.lengthOfMonth()
        val days: MutableList<DayOfMonth> = mutableListOf()

        viewModelScope.launch {
            for (i in 1..amountDays) {
                // loop trough every day in selected month
                val date = LocalDate.of(
                    state.value.selectedYear,
                    state.value.selectedMonth.ordinal + 1,
                    i
                )
                // get the mood of the current date out of the database
                val mood = dao.getMoodRecByDate(date.toString())?.mood
                val colour = mood?.moodColor ?: state.value.dayPassiveColour
                val moodName = mood?.name ?: "No mood logged for this day."
                // add it to the list
                days.add(
                    DayOfMonth(
                        colour = colour,
                        date = date,
                        mood = moodName
                    )
                )
            }
            _state.update {
                it.copy(
                    // update list in state
                    dayList = days
                )
            }
        }
    }
}
