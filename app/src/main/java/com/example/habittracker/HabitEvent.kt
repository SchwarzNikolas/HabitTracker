package com.example.habittracker


sealed interface HabitEvent {
    data class ModifyHabit(val habit: DisplayHabit, val frequency: Int): HabitEvent
    data class RecordHabitCompletion(val habit: DisplayHabit): HabitEvent
    data class BoxChecked(val habit: DisplayHabit, val n:Int): HabitEvent
}