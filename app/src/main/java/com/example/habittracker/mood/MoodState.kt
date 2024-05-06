package com.example.habittracker.mood

data class MoodState(
    val moods: List<MoodType> = enumValues<MoodType>().toList(),
    val selectedMood: MoodType = MoodType.OK,
    val moodColors: Map<MoodType, Long> = mapOf(
        MoodType.BAD to 0xFFFF0000,
        MoodType.SO_SO to 0xFFFFA500,
        MoodType.OK to 0xFFFFFF00,
        MoodType.ALRIGHT to 0xFF00FF00,
        MoodType.GOOD to 0xFF008000,
    )
)
