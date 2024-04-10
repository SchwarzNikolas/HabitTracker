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

        for (habit in state.habits){
            Text(text = habit.habit.value.name)
            Text(text = habit.habit.value.frequency.toString())
            Text(text = habit.done.toString())
            for (n in 0..<habit.habit.value.frequency){
                Text(text = habit.completion[n].value.toString())
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
        checked = habit.completion[n].value,
        onCheckedChange = { onEvent(HabitEvent.BoxChecked(habit, n))}
    )
}
