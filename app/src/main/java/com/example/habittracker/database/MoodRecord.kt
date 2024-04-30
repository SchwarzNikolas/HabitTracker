package com.example.habittracker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

// Database for (daily) mood record

@Entity
data class MoodRecord(
    @PrimaryKey(autoGenerate = true)
    val moodRecId: Int = 0,
    val date: String
)
