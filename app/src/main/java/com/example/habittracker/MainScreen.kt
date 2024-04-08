package com.example.habittracker

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun MainScreen (
    state: HabitState,
    onEvent: (HabitEvent) -> Unit
){

    Column()
    {
        for (habit in state.habits){
            Text(text = habit.name)
        }
    }

}