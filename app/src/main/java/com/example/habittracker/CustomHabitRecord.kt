package com.example.habittracker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CustomHabitRecord(
    @PrimaryKey (autoGenerate = true)
    val recordId: Int = 0,
    val name: String = "badminton",
    val date: String
)
