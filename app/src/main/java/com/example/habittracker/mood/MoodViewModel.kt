package com.example.habittracker.mood

import androidx.lifecycle.ViewModel
import com.example.habittracker.database.HabitDao
import kotlinx.coroutines.flow.MutableStateFlow

class MoodViewModel(
    private val dao: HabitDao
) : ViewModel() {
    private val _state = MutableStateFlow(MoodState())
    val state = _state
    fun onEvent(moodEvent: MoodEvent) {

    }
}