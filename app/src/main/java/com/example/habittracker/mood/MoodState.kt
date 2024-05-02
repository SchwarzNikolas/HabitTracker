package com.example.habittracker.mood

import com.example.habittracker.database.Mood
import java.time.LocalDateTime
import java.time.LocalTime

data class MoodState(
    // temporary
    val test: String = "test",

    val moods: List<MoodType> = enumValues<MoodType>().toList(),
    val selectedMood: MoodType = MoodType.OK
)
