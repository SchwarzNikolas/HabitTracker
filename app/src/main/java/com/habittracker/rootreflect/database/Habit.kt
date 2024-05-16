package com.habittracker.rootreflect.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// An entity represents a table.
// this entity represents a habit
@Entity(indices = [Index(value = ["name"], unique = true)])
data class Habit (
    @PrimaryKey(autoGenerate = true)
    val habitId: Int = 0,
    val name: String = "",
    val frequency: Int = 1,
    val completion: Int = 0,
    val done: Boolean = false,
    val occurrence: String = "1111111"
)