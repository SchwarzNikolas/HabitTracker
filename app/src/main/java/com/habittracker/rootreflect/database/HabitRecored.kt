package com.habittracker.rootreflect.database

import androidx.room.Entity
import java.time.LocalDate

// An entity represents a table.
// this entity represents a completed habit for a specific date
@Entity(primaryKeys = ["habitName", "date"])
data class HabitRecord(
    val habitName: String,
    val habitFrequency: Int,
    val date: LocalDate,
)