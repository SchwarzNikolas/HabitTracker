package com.example.habittracker

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
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
    private val _state = MutableStateFlow(HabitState())
    val state = _state

    init {
        viewModelScope.launch {

            inserHabit(Habit(name = "test1", frequency = 3))

            state.value.habits

            dao.fetchHabits().collect{habits -> run{
                val hablist = habits.stream().map { x -> DisplayHabit(habit = mutableStateOf(x))}.toList()
                _state.update { it.copy(habits = hablist) }
                }
            }
        }
    }

    fun onEvent(event: HabitEvent){
        when(event){
            is HabitEvent.ModifyHabit -> {
                viewModelScope.launch {
                    inserHabit(event.habit.habit.value.copy(frequency = event.frequency))
                }
            }

            is HabitEvent.RecordHabitCompletion -> {
                val record = HabitRecord(habitId = event.habit.habit.value.habitId, date = LocalDate.now().toString())
                viewModelScope.launch {
                    dao.insertRecord(record)
                }
            }

            is HabitEvent.BoxChecked -> {

                val index = state.value.habits.indexOf(event.habit)
                state.value.habits[index].completion[event.n].value = state.value.habits[index].completion[event.n].value.not()



                }
            }
        }


    private suspend fun inserHabit(habit: Habit){
        dao.insertHabit(habit)
    }


    private fun mapHabit(habits: MutableStateFlow<List<Habit>>):SnapshotStateList<DisplayHabit>{
        val mapedHabits = listOf<DisplayHabit>().toMutableList()

        for (habit in habits.value){
            mapedHabits.add(DisplayHabit(mutableStateOf(habit)))
        }
        return mapedHabits.toMutableStateList()
    }


    private suspend fun fetchHabits(test: MutableStateFlow<List<Habit>>) {
            dao.fetchHabits().collect(){habits -> test.value = habits}
    }
}

