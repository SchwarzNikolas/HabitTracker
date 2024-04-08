package com.example.habittracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HabitViewModel (
    private val dao: HabitDao
): ViewModel(){
    private  val _state = MutableStateFlow(HabitState())
    val state = _state
    init {
        viewModelScope.launch {


            val habits = fetchHabits()

            _state.update { it.copy(
                habits = habits
            ) }
        }
    }

    fun onEvent(event: HabitEvent){



    }


    private suspend fun inserHabit(habit: Habit){
        dao.insertHabit(habit)
    }

    private suspend fun fetchHabits():List<Habit>{
        return withContext(Dispatchers.IO){
            dao.fetchHabits()
        }
    }

}