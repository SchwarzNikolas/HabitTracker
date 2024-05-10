package com.habittracker.rootreflect.habit


import androidx.compose.runtime.mutableStateListOf
import com.habittracker.rootreflect.database.HabitRecord
import com.habittracker.rootreflect.mood.MoodType
import java.time.LocalDate


// This contains all the data that the UI displays
data class HabitState(

    // contains what habits were completed on which dates
    val habitRecord: MutableList<HabitRecord> = mutableStateListOf(),
    // string that will be displayed by the edit window
    val editString: String = "",
    // int that will be displayed by the edit window
    val editFreq: Int = 1,

    val editDisplayHabit: DisplayHabit = DisplayHabit(),

    val editDays: String = "",

    // list of all the habits that will be displayed
    val displayHabits: MutableList<DisplayHabit> = mutableStateListOf(),
    val weeklyDisplayHabits: MutableList<DisplayHabit> = mutableStateListOf(),


    //val date2: LocalDate = LocalDate.now().plusDays(1),
    // temp
    val date: LocalDate = LocalDate.now(),

    val selectedMood: MoodType = MoodType.OK,
    )