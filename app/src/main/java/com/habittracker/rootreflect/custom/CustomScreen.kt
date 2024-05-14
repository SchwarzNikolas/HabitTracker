package com.habittracker.rootreflect.custom

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomScreen(
    state: CustomState,
    onEvent: (CustomHabitEvent) -> Unit
){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Create new Habit!",
            fontSize = 24.sp
        )
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(
                text = "Weekly",
                modifier = Modifier.padding(end = 16.dp)
            )
            // Switch for daily/weekly habits
            SwitchHabit(state, onEvent)
            Text(
                text = "Daily",
                modifier = Modifier.padding(start = 16.dp)
            )
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

        Spacer(modifier = Modifier.weight(1f))

        HabitPreview(state = state)
    }
}

@Composable
fun EditWindow(onEvent: (CustomHabitEvent) -> Unit, state: CustomState) {
    val focusManager = LocalFocusManager.current
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        CustomTextField(
            value = state.habitName,
            label = "Name",
            onchange = {onEvent(CustomHabitEvent.EditName(it))},
            manager = focusManager
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Frequency")

        Slider(
            value = state.habitFrequency.toFloat(),
            onValueChange = { onEvent(CustomHabitEvent.EditFreq(it.toInt()))
            },
            valueRange = 1f..9f,
            steps = 7,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        // Spacer(modifier = Modifier.height(16.dp))

        if (!state.isDaily)
            WeeklyFields(state, onEvent, focusManager)

        // Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onEvent(CustomHabitEvent.SaveEdit)}
        ) {
            Text(
                text = "Save",
                fontSize = 24.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (state.habitName.isNotBlank()){
            HabitPreview(state = state)
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
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
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

        Spacer(modifier = Modifier.height(16.dp))
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
                containerColor = if (clicked) MaterialTheme.colorScheme.primary else Color.Transparent,
            )
        ) {}
    }
}


@Composable
fun CustomTextField(
    value: String,
    label: String,
    onchange: (String) -> Unit,
    manager: FocusManager
) {
    TextField(
        value = value,
        onValueChange = { onchange(it) },
        label = { Text(label) },
        colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent),
        keyboardOptions = KeyboardOptions.Default.copy(
            autoCorrectEnabled = true,
            imeAction = ImeAction.Done,
            showKeyboardOnFocus = true,
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(onDone = {
            manager.moveFocus(FocusDirection.Down) }),
        singleLine = true,
        modifier = Modifier
            //.height(50.dp)
            .fillMaxWidth(0.5f),
        shape = CircleShape
    )
}

@Composable
fun HabitPreview(state: CustomState){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val w = 300.dp
                val h = 50.dp
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BasicText(
                        text = state.habitName,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 10.dp),
                        style = LocalTextStyle.current.copy(fontSize = 30.sp)
                    )
                    Spacer(Modifier.weight(1f))
                }
                Row(modifier = Modifier.padding(bottom = 5.dp)) {
                    Box() {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .size(w, h)
                                .background(Color.DarkGray)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.Green)
                                .animateContentSize(
                                    animationSpec = tween(
                                        durationMillis = 500,
                                        easing = FastOutSlowInEasing
                                    )
                                )
                                .size(
                                    w * (1/2),
                                    h
                                )
                        )
                    }
                    Button(
                        onClick = {},
                        enabled = false,
                        modifier = Modifier.padding(start = 5.dp)
                    ) {
                        Text(text = state.habitFrequency.toString())
                    }
                }
            }
        }
    }
}