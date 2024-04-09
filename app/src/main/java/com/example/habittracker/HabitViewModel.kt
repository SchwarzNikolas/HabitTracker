package com.example.habittracker

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class HabitViewModel (
    private val dao: HabitDao
): ViewModel(){
    private  val _state = MutableStateFlow(HabitState())
    private val state2 = mutableStateListOf<DisplayHabit>()
    val state = _state

    init {
        viewModelScope.launch {

            //inserHabit(Habit(name = "test1", frequency = 3))

            val habits = mapHabit(fetchHabits())

            _state.update { it.copy(
                habits = habits
            )
            }
            state2.addAll(_state.value.habits)
        }
    }

    fun onEvent(event: HabitEvent){
        when(event){
            is HabitEvent.ModifyHabit -> {
                viewModelScope.launch {
                    inserHabit(event.habit.habit.copy(frequency = event.frequency))
                }
            }

            is HabitEvent.RecordHabitCompletion -> {
                val record = HabitRecord(habitId = event.habit.habit.habitId, date = LocalDate.now().toString())
                viewModelScope.launch {
                    dao.insertRecord(record)
                }
            }

            is HabitEvent.BoxChecked -> {

                val index = state2.indexOf(event.habit)
                state2.get(index).completion.set(event.n, false)

                state.update { it.copy(habits = state2) }

            }
        }
    }


    private suspend fun inserHabit(habit: Habit){
        dao.insertHabit(habit)
    }

    private fun mapHabit(habits: List<Habit>):MutableList<DisplayHabit>{
        val mapedHabits = mutableListOf<DisplayHabit>()
        for (habit in habits){
            mapedHabits.add(DisplayHabit(habit))
        }
        return mapedHabits
    }

    private suspend fun fetchHabits():List<Habit>{
        return withContext(Dispatchers.IO){
            dao.fetchHabits()
        }
    }
}

