package com.example.habittracker.mood

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun MoodScreen(
    state: MoodState,
    onEvent: (MoodEvent) -> Unit
) {
//    val currentTime = LocalTime.now()
//
//    if (currentTime.isBefore(state.notificationTime)) {
//        BeforeSetTimeScreen(state, onEvent)
//    } else {
//        AfterSetTimeScreen(state,onEvent)
//    }
}

@Composable
fun BeforeSetTimeScreen(state: MoodState, onEvent: (MoodEvent) -> Unit) {

}

@Composable
fun AfterSetTimeScreen(state: MoodState, onEvent: (MoodEvent) -> Unit) {

}