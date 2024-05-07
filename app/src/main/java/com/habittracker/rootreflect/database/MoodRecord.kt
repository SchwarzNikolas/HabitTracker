package com.habittracker.rootreflect.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.habittracker.rootreflect.mood.MoodType

// Database for (daily) mood record

@Entity
data class MoodRecord(
    @PrimaryKey(autoGenerate = true)
    val moodRecId: Int = 0,
    val moodDate: String = "2025-05-05",
    val mood: MoodType = MoodType.OK
)
