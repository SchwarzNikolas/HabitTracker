package com.example.habittracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CustomViewModel(
    private val dao: HabitDao
): ViewModel() {
    private val _state = MutableStateFlow(CustomState())
    // Why do we need this?
    val state = _state

    fun onEvent(event: CustomHabitEvent) {
        when (event) {
            is CustomHabitEvent.UpdateDaily -> {
                _state.update {
                    it.copy(
                        // toggle the state of isDaily
                        isDaily = _state.value.isDaily.not()
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
                val isDaily = state.value.isDaily
                val habitName = state.value.habitName
                val habitFrequency = state.value.habitFrequency

                if (habitName.isBlank() || habitFrequency == 0) {
                    return
                }

                val newCusHabit = Habit(
                    //isDaily = isDaily,      // We need to insert a field in Habit entity for this
                    name = habitName,
                    frequency = habitFrequency,
                )
                viewModelScope.launch {
                    dao.insertHabit(newCusHabit)
                }

                // Create an instance to DisplayHabit

                // customMode = false
            }
        }
    }
}