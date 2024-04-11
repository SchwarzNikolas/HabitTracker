package com.example.habittracker

import androidx.compose.runtime.MutableState
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


// modifies the state of the Habitstate
class HabitViewModel (
    private val dao: HabitDao
): ViewModel(){
    private val _state = MutableStateFlow(HabitState())
    val state = _state

    init {
        // On initiation
        viewModelScope.launch {

            insertHabit(Habit(name = "test1", frequency = 2))


            // sycs data base and state
            // will clean up later
            dao.fetchHabits().collect{habits -> run{
                val displayHabitList: MutableList<DisplayHabit> = mutableListOf()
                for (habit in habits) {
                    var wasFound = false
                    for (displayHabit in state.value.habits){
                        if (habit in displayHabit){
                            if (habit.frequency != displayHabit.completion.size){
                                val newCompletion: MutableList<MutableState<Boolean>> = if (habit.frequency < displayHabit.completion.size){
                                    displayHabit.completion.subList(0, habit.frequency)
                                }else{
                                    val n = habit.frequency - displayHabit.completion.size
                                    displayHabit.completion.addAll(MutableList(size = n){ mutableStateOf(false) })
                                    displayHabit.completion
                                }
                                displayHabitList.add(displayHabit.copy(habit = mutableStateOf(habit), completion = newCompletion))
                            }else {
                                displayHabitList.add(displayHabit.copy(habit = mutableStateOf(habit)))
                            }
                            wasFound = true
                            break
                        }
                    }
                    if (wasFound.not()){
                        displayHabitList.add(DisplayHabit(habit = mutableStateOf(habit)))
                    }
                }
                _state.update { it.copy(habits = displayHabitList) }
                }
            }
        }
    }

    // Handles the events triggered by the UI
    fun onEvent(event: HabitEvent){
        when(event){
            is HabitEvent.ModifyHabit -> {
                viewModelScope.launch {
                    insertHabit(event.habit.habit.value.copy(frequency = event.frequency))
                }
            }

            is HabitEvent.RecordHabitCompletion -> {
                val record = HabitRecord(habitId = event.habit.habit.value.habitId, date = LocalDate.now().toString())
                viewModelScope.launch {
                    dao.insertRecord(record)
                }
            }

            is HabitEvent.BoxChecked -> {
                event.habit.value = event.habit.value.not()
                }
            }
        }


    private suspend fun insertHabit(habit: Habit){
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

