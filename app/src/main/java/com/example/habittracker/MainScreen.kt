package com.example.habittracker

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun MainScreen (
    state: HabitState,
    onEvent: (HabitEvent) -> Unit
){

    Column()
    {
        Text(text = state.name)
        for (habit in state.habits){
            Text(text = habit.habit.name)
            Text(text = habit.habit.frequency.toString())
            Text(text = habit.done.toString())
            for (n in 0..<habit.habit.frequency){
                Text(text = habit.completion[n].toString())
                CheckBoxDemo(habit, n, onEvent)
            }

            Button(onClick = {onEvent(HabitEvent.ModifyHabit(habit, 1))}) {
                Text(text = "edit")
            }
        }
    }
}


@Composable
fun CheckBoxDemo(habit: DisplayHabit, n: Int, onEvent: (HabitEvent) -> Unit) {
    Checkbox(
        checked = habit.completion[n],
        onCheckedChange = { onEvent(HabitEvent.BoxChecked(habit, n))}
    )
}
