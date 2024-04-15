package com.example.habittracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

@Composable
fun MainScreen (
    state: HabitState,
    onEvent: (HabitEvent) -> Unit
){

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        for (habit in state.habits){
            ElevatedHabit(habit, onEvent)
            Button(onClick = {onEvent(HabitEvent.ModifyHabit(habit, 3))}) {
                Text(text = "edit")
            }
        }
    }
}


@Composable
fun ElevatedHabit(displayHabit: DisplayHabit, onEvent: (HabitEvent) -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 380.dp, height = 100.dp),
        colors = CardDefaults.cardColors(Color.Green)

    ) {
        Column {

            Row {
                for (n in 0..<displayHabit.habit.value.frequency) {
                    HabitCheckBox(displayHabit, n, onEvent)
                }
                Text(
                    text = displayHabit.habit.value.name,
                    modifier = Modifier
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
            Text(text = "test")
            Row {
                Button(onClick = { onEvent(HabitEvent.EditHabit(displayHabit)) }) {
                    Text(text = "edit")
                }

            }
        }
    }
}

@Composable
fun HabitCheckBox(displayHabit: DisplayHabit, index:Int, onEvent: (HabitEvent) -> Unit){
    Checkbox(
        checked = displayHabit.completion[index].value,
        onCheckedChange = {onEvent(HabitEvent.BoxChecked(displayHabit, index))},
        modifier = Modifier.padding(5.dp),
        colors = CheckboxDefaults.colors(Color.Green)
    )
}
