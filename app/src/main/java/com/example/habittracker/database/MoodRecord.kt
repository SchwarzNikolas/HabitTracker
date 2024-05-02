package com.example.habittracker.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.habittracker.mood.MoodType

// Database for (daily) mood record

@Entity
data class MoodRecord(
    @PrimaryKey(autoGenerate = true)
    val moodRecId: Int = 0,
    val moodDate: String = "2025-05-05",
    val mood: MoodType = MoodType.OK
)
