package com.example.habittracker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Habit (
    @PrimaryKey(autoGenerate = true)
    val habitId: Int = 0,
    val name: String = "test"
)