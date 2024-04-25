package com.example.habittracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.database.Habit
import com.example.habittracker.database.HabitCompletion
import com.example.habittracker.database.HabitDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onCompletion
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

            is CustomHabitEvent.ToggleDay -> {
                _state.update {
                    state ->
                    val newCompletion = state.completion.toMutableList()
                    newCompletion[event.dayIndex].value = !newCompletion[event.dayIndex].value
                    state.copy(completion = newCompletion)
                }
            }

            is CustomHabitEvent.SaveEdit -> {
                val test = "1111111" // this represent completion and with daily is every day
                if(state.value.isDaily){
                    val sb = StringBuilder(test)
                    sb.clear()
                    for(day in state.value.completion){
                        sb.append(day)
                    }
                }
                val habitName = state.value.habitName
                val habitFrequency = state.value.habitFrequency

                if (habitName.isBlank() || habitFrequency.isBlank()) {
                    return
                }

                val newCusHabit = Habit(
                    //isDaily = isDaily,      // We need to insert a field in Habit entity for this
                    name = habitName,
                    frequency = habitFrequency.toInt(),
                )
                viewModelScope.launch {
                    dao.insertHabit(newCusHabit)
                    dao.insertCompletion(HabitCompletion(occurrence = test))
                }
            }
        }
    }
}