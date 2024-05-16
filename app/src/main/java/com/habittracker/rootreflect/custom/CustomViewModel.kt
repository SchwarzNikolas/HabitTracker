package com.habittracker.rootreflect.custom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habittracker.rootreflect.database.Habit
import com.habittracker.rootreflect.database.HabitDao
import kotlinx.coroutines.delay
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
                    occurrence = habitOccurrence
                )
                var text = "Habit successfully added "
                viewModelScope.launch {
                    try {
                        dao.insertHabit(newCusHabit)
                    }catch (e: Exception){
                        text = "Error: Name already Exists"
                        return@launch
                    }finally {
                        state.update { it.copy(notificationVisibility = true,
                            notificationText = text) }
                    }
                    //dao.upsertCompletion(HabitCompletion(habitName = habitName ,occurrence = habitOccurrence))
                }
            }
            is CustomHabitEvent.KeyboardFocus -> {
                _state.update {
                    it.copy(
                        keyboardFocus = event.isFocused
                    )
                }
            }

            CustomHabitEvent.ToggleNotificationVisibility -> {
                viewModelScope.launch {
                    delay(2000)
                    state.update { it.copy(
                        notificationVisibility = false
                    ) }
                }
            }
        }
    }
}