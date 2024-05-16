package com.habittracker.rootreflect.custom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habittracker.rootreflect.database.Habit
import com.habittracker.rootreflect.database.HabitCompletion
import com.habittracker.rootreflect.database.HabitDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CustomViewModel(
    private val dao: HabitDao
): ViewModel() {
    private val _state = MutableStateFlow(CustomState())
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

            is CustomHabitEvent.ToggleDay -> {
                _state.update {
                    state ->
                    val newCompletion = state.completion.toMutableList()
                    newCompletion[event.dayIndex].value = !newCompletion[event.dayIndex].value
                    state.copy(completion = newCompletion)
                }
            }

            is CustomHabitEvent.SaveEdit -> {

                val habitOccurrence: String = if (state.value.isDaily) {
                    "1111111"
                }
                else {
                    val sb = StringBuilder("0000000")
                    for (i in 0 until 7) {
                        if (state.value.completion[i].value) {
                            sb.setCharAt(i, '1')
                        }
                    }
                    sb.toString()
                }

                val habitName = state.value.habitName
                // if weekly habits have also frequency
                val habitFrequency: Int = state.value.habitFrequency

                if (habitName.isBlank()) {
                    return
                }

                val newCusHabit = Habit(
                    name = habitName,
                    frequency = habitFrequency,
                )
                viewModelScope.launch {
                    dao.upsertHabit(newCusHabit)
                    dao.upsertCompletion(HabitCompletion(occurrence = habitOccurrence))
                }
            }
            is CustomHabitEvent.KeyboardFocus -> {
                _state.update {
                    it.copy(
                        keyboardFocus = event.isFocused
                    )
                }
            }
            is CustomHabitEvent.ToggleDialog -> {
                _state.update {
                    it.copy(
                        showDialog = state.value.showDialog.not()
                    )
                }
            }
        }
    }
}