package com.example.habittracker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.habittracker.habit.CustomTextField

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
            // Text shown is based on switch state
            Text(text = if (state.isDaily) "Daily Habit" else "Weekly Habit")
            // Switch for daily/weekly habits
            SwitchHabit(state, onEvent)
        }
        TextField(
            value = "value",
            onValueChange = { onEvent(CustomHabitEvent.EditName(it)) },
            label = { Text("label") },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                //cursorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrectEnabled = true,
                imeAction = ImeAction.Done,
                showKeyboardOnFocus = null ?: true
            ),
        )

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

        if (!state.isDaily)
            WeeklyFields(state, onEvent, focusManager)

        Row (modifier = Modifier.size(width = 380.dp, height = 30.dp)) {
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

@Composable
fun WeeklyFields(state: CustomState, onEvent: (CustomHabitEvent) -> Unit, focusManager: FocusManager) {
    Column {
        // Every day switch
        /*Row (verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Every day", modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(16.dp))
            // have to implement
            Switch(
                checked = state.isEveryday,
                onCheckedChange = null,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }*/

        Text("Days")

        // Days buttons
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (index in 0 until 7) {
                val day = listOf("M", "T", "W", "T", "F", "S", "S")[index]
                val clicked = state.completion[index].value
                DayButton(day, clicked, onEvent, index)
            }
        }
    }
}

@Composable
fun DayButton(
    day: String,
    clicked: Boolean,
    onEvent: (CustomHabitEvent) -> Unit,
    dayIndex: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = day)
        OutlinedButton(
            onClick = { onEvent(CustomHabitEvent.ToggleDay(dayIndex)) },
            modifier = Modifier
                .size(width = 40.dp, height = 40.dp),
            border = BorderStroke(1.dp, Color.Black),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (clicked) Color.Black else Color.White,
            )
        ) {}
    }
}