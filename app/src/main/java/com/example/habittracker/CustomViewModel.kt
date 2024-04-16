package com.example.habittracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CustomViewModel(
    private val cusDao: CustomHabitDao
): ViewModel() {
    private val _state = MutableStateFlow(CustomState())

    fun onEvent(event: CustomHabitEvent) {
        when (event) {
            is CustomHabitEvent.BoxChecked -> {
            }

            is CustomHabitEvent.CancelEdit -> TODO()
            is CustomHabitEvent.EditFreq -> TODO()
            is CustomHabitEvent.EditName -> TODO()
            is CustomHabitEvent.HideDialog -> TODO()
            is CustomHabitEvent.SaveEdit -> TODO()
            is CustomHabitEvent.ShowDialog -> TODO()
            is CustomHabitEvent.DeleteCusHabit -> {
                viewModelScope.launch {
                    cusDao.deleteCusHabit(event.customDisplayHabit.cusHabit.value)
                }
            }
        }
    }
}