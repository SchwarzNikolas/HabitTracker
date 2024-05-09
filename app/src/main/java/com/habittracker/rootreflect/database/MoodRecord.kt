package com.habittracker.rootreflect.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.habittracker.rootreflect.mood.MoodType
import java.time.LocalDate

// Database for (daily) mood record

@Entity
data class MoodRecord(
    @PrimaryKey
    val moodDate: LocalDate,
    val mood: MoodType = MoodType.OK
)
