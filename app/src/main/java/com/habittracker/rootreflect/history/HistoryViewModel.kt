package com.habittracker.rootreflect.history

import androidx.lifecycle.ViewModel
import com.habittracker.rootreflect.database.HabitDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class HistoryViewModel(
      private val dao: HabitDao
): ViewModel() {
    private val _state = MutableStateFlow(HistoryState())
    val state = _state

    fun onEvent(event: HistoryEvent) {
        /*
        when (event) {
            is HistoryEvent.Eventname -> {
                _state.update {
                    it.copy(
                        // change value
                    )
                }
            }
        }
         */
    }
}