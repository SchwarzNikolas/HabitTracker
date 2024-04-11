package com.example.habittracker

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

// An entity represents a table
@Entity
data class HabitRecord(
    @PrimaryKey(autoGenerate = true)
    val recordId: Int = 0,
    val habitId: Int,
    val date: String,
)