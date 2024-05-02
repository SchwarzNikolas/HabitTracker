package com.example.habittracker.mood

import com.example.habittracker.database.Mood

// Events that happen in the MoodBar
sealed interface MoodEvent {
    data class BadSelected(val moodType: MoodType = MoodType.BAD): MoodEvent
    data class SoSoSelected(val moodType: MoodType = MoodType.SO_SO): MoodEvent
    data class OkSelected(val moodType: MoodType = MoodType.OK): MoodEvent
    data class AlrightSelected(val moodType: MoodType = MoodType.ALRIGHT): MoodEvent
    data class GoodSelected(val moodType: MoodType = MoodType.GOOD): MoodEvent
}