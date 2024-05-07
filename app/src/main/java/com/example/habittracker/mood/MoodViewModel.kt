package com.example.habittracker.mood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.database.HabitDao
import com.example.habittracker.database.MoodRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class MoodViewModel(
    private val dao: HabitDao
) : ViewModel() {
    private val _state = MutableStateFlow(MoodState())
    val state = _state

    private val currentDate: LocalDate = LocalDate.now()

    fun onEvent(moodEvent: MoodEvent) {
        when (moodEvent) {
            is MoodEvent.BadSelected -> {
                _state.update {
                    it.copy(
                        selectedMood = moodEvent.moodType
                    )
                }
                viewModelScope.launch {
                    val existingRec = dao.getMoodRecByDate(currentDate.toString())
                    if (existingRec == null) {
                        dao.insertMoodRec(moodRec = MoodRecord(
                            moodDate = currentDate.toString(),
                            mood = moodEvent.moodType))
                    } else {
                            dao.updateMoodRec(currentDate.toString(), moodEvent.moodType)
                    }
                }
            }
            is MoodEvent.SoSoSelected -> {
                _state.update {
                    it.copy(
                        selectedMood = moodEvent.moodType
                    )
                }
                viewModelScope.launch {
                    val existingRec = dao.getMoodRecByDate(currentDate.toString())
                    if (existingRec == null) {
                        dao.insertMoodRec(moodRec = MoodRecord(
                            moodDate = currentDate.toString(),
                            mood = moodEvent.moodType))
                    } else {
                        dao.updateMoodRec(currentDate.toString(), moodEvent.moodType)
                    }
                }
            }
            is MoodEvent.OkSelected -> {
                _state.update {
                    it.copy(
                        selectedMood = moodEvent.moodType
                    )
                }
                viewModelScope.launch {
                    dao.deleteMoodRecord(
                        currentDate.toString())
                }
            }
            is MoodEvent.AlrightSelected -> {
                _state.update {
                    it.copy(
                        selectedMood = moodEvent.moodType
                    )
                }
                viewModelScope.launch {
                    val existingRec = dao.getMoodRecByDate(currentDate.toString())
                    if (existingRec == null) {
                        dao.insertMoodRec(moodRec = MoodRecord(
                            moodDate = currentDate.toString(),
                            mood = moodEvent.moodType))
                    } else {
                        dao.updateMoodRec(currentDate.toString(), moodEvent.moodType)
                    }
                }
            }
            is MoodEvent.GoodSelected -> {
                _state.update {
                    it.copy(
                        selectedMood = moodEvent.moodType
                    )
                }
                viewModelScope.launch {
                    val existingRec = dao.getMoodRecByDate(currentDate.toString())
                    if (existingRec == null) {
                        dao.insertMoodRec(moodRec = MoodRecord(
                            moodDate = currentDate.toString(),
                            mood = moodEvent.moodType))
                    } else {
                        dao.updateMoodRec(currentDate.toString(), moodEvent.moodType)
                    }
                }
            }
        }
    }
}