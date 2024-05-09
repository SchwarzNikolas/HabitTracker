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
                /*val test = "1111111" // this represent on which days the habit will show up
                if(state.value.isDaily){
                    val sb = StringBuilder(test)
                    sb.clear()
                    for(day in state.value.completion){
                        sb.append(day)
                    }
                }*/

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
                val habitFrequency: String = state.value.habitFrequency

                // if weekly habits don't have frequency
                /*val habitFrequency: String = if (state.value.isDaily) {
                    state.value.habitFrequency
                }
                else {
                    //if (habitOccurrence.contains("0")) {
                    //    "0"
                    //}
                    //else {
                        "1" // delete then frequency form weekly creation
                    //}
                }*/

                if (habitName.isBlank() || habitFrequency.isBlank()) {
                    return
                }

                val newCusHabit = Habit(
                    name = habitName,
                    frequency = habitFrequency.toInt(),
                )
                viewModelScope.launch {
                    dao.upsertHabit(newCusHabit)
                    dao.upsertCompletion(HabitCompletion(occurrence = habitOccurrence))
                }
            }
        }
    }
}