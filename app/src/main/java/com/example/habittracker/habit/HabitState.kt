package com.example.habittracker.habit


import androidx.compose.runtime.mutableStateListOf
import com.example.habittracker.database.Habit
import com.example.habittracker.database.HabitCompletion
import com.example.habittracker.database.HabitJoin
import com.example.habittracker.database.HabitRecord
import kotlinx.coroutines.Job
import java.time.LocalDate
import java.time.LocalDateTime


// This contains all the data that the UI displays
data class HabitState(
    // list of all the habits that will be displayed
    val displayHabits: MutableList<DisplayHabit> = mutableStateListOf(),
    val weeklyDisplayHabits: MutableList<DisplayHabit> = mutableStateListOf(),
    // contains what habits were completed on which dates
    val habitRecord: MutableList<HabitRecord> = mutableStateListOf(),
    // string that will be displayed by the edit window
    val editString: String = "",
    // int that will be displayed by the edit window
    val editFreq: Int = 1,
    // Deprecated
    // habit that is currently being edited
    val editHabit: Habit = Habit(),
    // Deprecated
    // bool of whether or not the edit window is being displayed
    val showEdit: Boolean = false,

    val habitJoin: MutableList<HabitJoin> = mutableListOf(),

    val edithabitJoin: HabitJoin = HabitJoin(Habit(), HabitCompletion()),

    var date: LocalDateTime = LocalDateTime.now(),

    val day: Int = LocalDate.now().dayOfWeek.value-1,
    var job: Job

)