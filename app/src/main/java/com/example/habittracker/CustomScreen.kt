package com.example.habittracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp

@Composable
fun CustomScreen(
    state: CustomState,
    onEvent: (CustomHabitEvent) -> Unit
){
    Column {
        Text(text = "Create Habit Here!")
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Daily Habit")
            SwitchHabit(state, onEvent)
        }
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EditWindow(onEvent, state)
        }
        
    }
}

@Composable
fun EditWindow(onEvent: (CustomHabitEvent) -> Unit, state: CustomState) {
    val focusManager = LocalFocusManager.current
    Column {

        CustomTextField(
            value = state.habitName,
            label = "Name",
            onchange = {onEvent(CustomHabitEvent.EditName(it))},
            manager = focusManager
        )

        CustomTextField(
            value = state.habitFrequency,
            label = "Frequency",
            onchange = {onEvent(CustomHabitEvent.EditFreq(it))},
            manager = focusManager
        )
        Row (modifier = Modifier.size(width = 380.dp, height = 30.dp)) {
            Button(onClick = { onEvent(CustomHabitEvent.CancelEdit)}) {
                Text(text = "Cancel")
            }
            Button(onClick = { onEvent(CustomHabitEvent.SaveEdit)}) {
                Text(text = "Save")
            }

        }
    }
}


@Composable
fun SwitchHabit(state: CustomState, onEvent: (CustomHabitEvent) -> Unit) {
    Switch(
        checked = state.isDaily,
        onCheckedChange = { onEvent(CustomHabitEvent.UpdateDaily)},
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.primary,
            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
            uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
            uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
        )
    )
}