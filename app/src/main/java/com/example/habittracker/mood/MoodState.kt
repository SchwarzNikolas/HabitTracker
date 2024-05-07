package com.example.habittracker.mood

data class MoodState(
    val moods: List<MoodType> = enumValues<MoodType>().toList(),
    val selectedMood: MoodType = MoodType.OK,
)
