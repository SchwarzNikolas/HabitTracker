package com.example.habittracker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

// An entity represents a table.
// this entity represents a habit
@Entity
data class Habit (
    @PrimaryKey(autoGenerate = true)
    val habitId: Int = 0,
    val name: String = "test123",
    val frequency: Int = 1,
    val occurrence: String = "1111111"
)