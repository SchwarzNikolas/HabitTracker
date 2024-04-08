package com.example.habittracker

data class HabitState (
    val name: String = "test",
    val habits: List<Habit> = emptyList()
){
}