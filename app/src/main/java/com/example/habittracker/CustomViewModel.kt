package com.example.habittracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CustomViewModel(
    private val dao: HabitDao
): ViewModel() {
    private val _state = MutableStateFlow(CustomState())
    // Why do we need this?
    val state = _state.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000),CustomState())

    fun onEvent(event: CustomHabitEvent) {
        when (event) {
            is CustomHabitEvent.switchSwitched -> {
                _state.update {
                    it.copy(
                        // here event.isWeekly returns true, see CustomHabitEvent
                        isWeekly = state.value.isWeekly.not()
                    )
                }
            }
            is CustomHabitEvent.EditName -> {
                _state.update {
                    it.copy(
                        // the state name is changed to the name from EditName event field
                        habitName = event.name
                    )
                }
            }
            is CustomHabitEvent.EditFreq -> {
                _state.update {
                    it.copy(
                        habitFrequency = event.frequency
                    )
                }
            }
            is CustomHabitEvent.CancelEdit -> {
                _state.update {
                    it.copy(
                        customMode = false
                    )
                }
            }
            is CustomHabitEvent.SaveEdit -> {
                //
                val isWeekly = state.value.isWeekly
                val habitName = state.value.habitName
                val habitFrequency = state.value.habitFrequency

                if (habitName.isBlank() || habitFrequency == 0) {
                    return
                }

                val newCusHabit = Habit(
                    //isWeekly = isWeekly,      // We need to insert a field in Habit entity for this
                    name = habitName,
                    frequency = habitFrequency,
                )
                viewModelScope.launch {
                    dao.insertHabit(newCusHabit)
                }
                // customMode = false
            }
        }
    }
}