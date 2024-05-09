package com.habittracker.rootreflect.mood

// Events that happen in the MoodBar
sealed interface MoodEvent {
    data class MoodChange(val moodType: MoodType): MoodEvent

}