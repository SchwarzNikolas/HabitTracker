package com.example.habittracker

data class DisplayHabit (
    val habit: Habit,
    val done:Boolean = false,
    val completion: MutableList<Boolean> = MutableList(size = habit.frequency){true}
)