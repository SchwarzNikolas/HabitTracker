package com.habittracker.rootreflect.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.habittracker.rootreflect.habit.HabitEvent
import java.time.LocalDate

// An entity represents a table.
// this entity represents a completed habit for a specific date
@Entity
data class HabitRecord(
    @PrimaryKey(autoGenerate = true)
    val recordId: Int = 0,
    val habitName: String,
    val habitFrequency: Int,
    val date: LocalDate,
)