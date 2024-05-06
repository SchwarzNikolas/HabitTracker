package com.example.habittracker.mood

data class MoodState(
    // temporary
    val test: String = "test",

    val moods: List<MoodType> = enumValues<MoodType>().toList(),
    val selectedMood: MoodType = MoodType.OK
)
