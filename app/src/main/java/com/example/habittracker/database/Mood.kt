package com.example.habittracker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

// Database table for Mood
@Entity
data class Mood(
    @PrimaryKey(autoGenerate = true)
    val moodId: Int = 0,
    val name: String = "Pumped",
    val moodColor: String = "Purple"
)

