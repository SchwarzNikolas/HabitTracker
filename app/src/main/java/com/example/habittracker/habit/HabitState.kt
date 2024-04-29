package com.example.habittracker.habit


import androidx.compose.runtime.mutableStateListOf
import com.example.habittracker.database.HabitRecord
import kotlinx.coroutines.Job
import java.time.LocalDateTime


// This contains all the data that the UI displays
data class HabitState(

    // contains what habits were completed on which dates
    val habitRecord: MutableList<HabitRecord> = mutableStateListOf(),
    // string that will be displayed by the edit window
    val editString: String = "",
    // int that will be displayed by the edit window
    val editFreq: Int = 1,

    val editDisplayHabit: DisplayHabit = DisplayHabit(),

    // list of all the habits that will be displayed
    val displayHabits: MutableList<DisplayHabit> = mutableStateListOf(),
    val weeklyDisplayHabits: MutableList<DisplayHabit> = mutableStateListOf(),


    val date2: LocalDateTime = LocalDateTime.now().plusDays(1),
    // temp
    val date: LocalDateTime = LocalDateTime.now(),

    var job: Job

)