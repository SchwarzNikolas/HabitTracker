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
        ElevatedHabit("Go for a walk")
        for (habit in state.habits){
            Text(text = habit.habit.value.name)
            Text(text = habit.habit.value.frequency.toString())
            Text(text = habit.done.toString())
            for (n in 0..<habit.habit.value.frequency){
                Text(text = habit.completion[n].value.toString())
                //CheckBoxDemo(habit.completion[n], onEvent)
            }

            Button(onClick = {onEvent(HabitEvent.ModifyHabit(habit, 3))}) {
                Text(text = "edit")
            }
        }
    }
}


@Composable
fun HabitCheckBox(){
    Row(
    ){
        val checkedState = remember {
            mutableStateOf(false)
        }
            Checkbox(
                checked = checkedState.value,
                onCheckedChange = {checkedState.value = it},
                modifier = Modifier.padding(5.dp),
                colors = CheckboxDefaults.colors(Color.Green)
            )

    }
}


@Composable
fun ElevatedHabit(text: String) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 380.dp, height = 65.dp)

    ) {
        Row{
            HabitCheckBox()
            Text(
            text = text,
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center
            )
        }
    }
}
