package com.habittracker.rootreflect.history

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habittracker.rootreflect.database.HabitDao
import com.habittracker.rootreflect.database.HabitRecord
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

class HistoryViewModel(
      private val dao: HabitDao
): ViewModel() {
    private val _state = MutableStateFlow(HistoryState())
    private var job:Job? = null
    val state = _state

    init {
        viewModelScope.launch {
            dao.fetchDates().collect { dates ->
                run {
                    // recordedDates has this format: yyyymm as an integer
                    val recordedDates: MutableSet<Int> = mutableSetOf()
                    val filledDates: MutableList<Int> = mutableStateListOf()
                    for (date in dates) {
                        recordedDates.add(date.year*100 + date.month.value)
                    }
                    if (recordedDates.size > 1) {
                        IntRange(recordedDates.min(), recordedDates.max()).forEach { date ->
                            if (date % 100 < 13 && date % 100 != 0) {
                                filledDates.add(date)
                            }
                        }
                    }
                    else if (recordedDates.size == 1){
                        filledDates.add(recordedDates.last())
                    }
                    else{
                        filledDates.add(LocalDate.now().year * 100 + LocalDate.now().month.value)
                    }
                    _state.update {
                        it.copy(
                            monthsWithRecord = filledDates
                        )
                    }
                }
                updateDays()
            }
        }
    }

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.SelectPlant -> {
                _state.update {
                    it.copy(
                        bottomSheetActive = true,
                        habitInfo = true,
                        habitStored = event.recordedHabit
                    )
                }
            }
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
                // change selected month and year and update the calendar accordingly
                _state.update {
                    it.copy(
                        selectedMonth = Month.of(event.date%100),
                        selectedYear = (event.date - event.date%100) / 100
                    )
                }
                updateDays()
            }
            is HistoryEvent.ChangeSelectedDay -> {
                /*
                change selected date (is invoked when user selects a day in the calendar)
                additionally this event changes the selected mood and will change what the bottom
                sheet should display. It also fetches the completed habits from the selected day.
                 */
                if (job != null){
                    job!!.cancel()
                }
                job = viewModelScope.launch {
                    dao.fetchHabitRecordsByDate(event.day).collect {
                    habitRecords ->
                        run {
                            val records: MutableList<HabitRecord> = mutableStateListOf()
                            for (record in habitRecords){
                                records.add(record)
                            }
                            _state.update {
                                it.copy(
                                    habitList = records,
                                    selectedDate = event.day,
                                    selectedMood = event.moodName,
                                    habitInfo = false,
                                    // filter the habits by their frequency into three separate lists
                                    habitListF1 = records.filter {record -> record.habitFrequency == 1 }.toList().toMutableList(),
                                    habitListF2 = records.filter {record -> record.habitFrequency == 2 }.toList().toMutableList(),
                                    habitListF3Above = records.filter {record -> record.habitFrequency >= 3 }.toList().toMutableList(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateDays(){
        /*
        method which fetches all logged moods for a certain month and updates a list of days in the state
         */
        // change to selected year instead of current year
        val year: YearMonth = YearMonth.of(state.value.selectedYear, state.value.selectedMonth)
        val amountDays: Int = year.lengthOfMonth()
        val days: MutableList<DayOfMonth> = mutableListOf()

        viewModelScope.launch {
            for (i in 1..amountDays) {
                // loop trough every day in selected month
                val date = LocalDate.of(
                    state.value.selectedYear,
                    state.value.selectedMonth.value,
                    i
                )
                // get the mood of the current date out of the database
                val mood = dao.getMoodRecByDate(date.toString())?.mood
                val colour = mood?.moodColor ?: state.value.dayPassiveColour
                val moodName = mood?.name ?: "No mood"
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
