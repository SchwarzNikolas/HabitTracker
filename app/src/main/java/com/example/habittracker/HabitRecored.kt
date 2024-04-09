package com.example.habittracker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HabitRecord (
    @PrimaryKey
    val recordId: Int = 0,
    val habitId: Int,
    val completed: Boolean
)