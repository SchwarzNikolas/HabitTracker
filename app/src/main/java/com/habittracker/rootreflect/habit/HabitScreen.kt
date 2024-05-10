package com.habittracker.rootreflect.habit

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habittracker.rootreflect.mood.MoodEvent
import com.habittracker.rootreflect.mood.MoodType


// details about compose available at https://developer.android.com/develop/ui/compose/layouts/basics

data class Item(
    val name: String,
    val onClick: () -> Unit,
    val icon: ImageVector
    )
private val moods: List<MoodType> = enumValues<MoodType>().toList()
@Composable
fun MainScreen (
    state: HabitState,
    onEvent: (HabitEvent) -> Unit,
    moodEvent: (MoodEvent) -> Unit
){
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        // Mood part of the screen
        MoodSection(state, moodEvent, Modifier)

        Divider(modifier = Modifier
            .fillMaxWidth()
            .width(4.dp))

        LazyVerticalGrid(columns = GridCells.Fixed(1) ) {
            // Daily Habits
            items(state.displayHabits.size){
                BarHabit(
                    displayHabit = state.displayHabits[it],
                    onEvent = onEvent,
                    state
                )
            }
            // Weekly Habits
            items(state.weeklyDisplayHabits.size){
                BarHabit(
                    displayHabit = state.weeklyDisplayHabits[it],
                    onEvent = onEvent,
                    state
                )
            }
        }


        // debug
        Button(onClick = { onEvent(HabitEvent.ResetCompletion)}) {
                Text(text = "test")
        }
        Button(onClick = { onEvent(HabitEvent.NextDay)}) {
            Text(text = "next day")
        }
        Text(text = state.date.dayOfWeek.toString())
        //Text(text = state.date2.dayOfWeek.toString())

        Text(text = state.habitRecord.size.toString())
        for (habitRecord in state.habitRecord) {
            Text(text = habitRecord.habitName)
            Text(text = habitRecord.date.toString())
        }
    }
}

@Composable
fun BarHabit(displayHabit: DisplayHabit, onEvent: (HabitEvent) -> Unit, state: HabitState){
    if (displayHabit.beingEdited.value) {
        EditMode(onEvent = onEvent, displayHabit = displayHabit, state = state)
    } else {
        DisplayMode(onEvent = onEvent, displayHabit = displayHabit)
    }
}


@Composable
fun DisplayMode(onEvent: (HabitEvent) -> Unit, displayHabit: DisplayHabit){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .clickable { onEvent(HabitEvent.IncCompletion(displayHabit.habitJoin)) }
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 5.dp),
        colors = CardDefaults.cardColors(Color.Gray)
    ) {
        Box {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val dropdownItems: List<Item> = listOf(
                    Item(
                        name = "Edit",
                        onClick = { onEvent(HabitEvent.EditHabit(displayHabit)) },
                        Icons.Default.Edit
                    ),
                    Item(
                        name = "Undo",
                        onClick = { onEvent(HabitEvent.DecCompletion(displayHabit.habitJoin)) },
                        Icons.Default.ArrowBack
                    ),
                    Item(
                        name = "Delete",
                        onClick = { onEvent(HabitEvent.DeleteHabit(displayHabit.habitJoin)) },
                        Icons.Default.Delete
                    )
                )
                val w = 300.dp
                val h = 50.dp

                Row(verticalAlignment = Alignment.CenterVertically) {
                    BasicText(
                        text = displayHabit.habitJoin.habit.name,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 10.dp),
                        style = LocalTextStyle.current.copy(fontSize = 30.sp)
                    )
                    Spacer(Modifier.weight(1f))
                    IconButton(
                        onClick = { onEvent(HabitEvent.ContextMenuVisibility(displayHabit)) },
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "ContextMenu"
                        )
                        DropdownMenu(
                            expanded = displayHabit.isMenuVisible.value,
                            onDismissRequest = {
                                onEvent(
                                    HabitEvent.ContextMenuVisibility(
                                        displayHabit
                                    )
                                )
                            }
                        ) {
                            dropdownItems.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item.name) },
                                    onClick = {
                                        item.onClick()
                                        onEvent(HabitEvent.ContextMenuVisibility(displayHabit))
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = null
                                        )
                                    }
                                )
                            }
                        }
                    }
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
                                    w * (displayHabit.habitJoin.completion.completion.toFloat() / displayHabit.habitJoin.habit.frequency),
                                    h
                                )
                        )
                    }
                    Button(
                        onClick = {},
                        enabled = false,
                        modifier = Modifier.padding(start = 5.dp)
                    ) {
                        Text(text = displayHabit.habitJoin.habit.frequency.toString())
                    }
                }
            }
        }
    }
}


@Composable
fun EditMode(onEvent: (HabitEvent) -> Unit, displayHabit: DisplayHabit, state: HabitState) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 5.dp),
        colors = CardDefaults.cardColors(Color.Gray)
    ) {
        Box {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val w = 300.dp
                    val h = 50.dp

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BasicTextField(
                            value = state.editString,
                            onValueChange = { onEvent(HabitEvent.UpDateEditString(it)) },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                autoCorrectEnabled = true,
                                imeAction = ImeAction.Done,
                                showKeyboardOnFocus = true,
                                capitalization = KeyboardCapitalization.Sentences,
                                keyboardType = KeyboardType.Text
                            ),
                            maxLines = 2,
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 30.sp,
                                textDecoration = TextDecoration.Underline
                            ),
                            modifier = Modifier.padding(start = 10.dp),
                        )
                        Spacer(Modifier.weight(1f))
                        IconButton(
                            onClick = { onEvent(HabitEvent.ModifyHabit(displayHabit.habitJoin)) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "ContextMenu"
                            )
                        }
                        IconButton(onClick = { onEvent(HabitEvent.CancelEdit(displayHabit)) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "cancel edit"
                            )
                        }
                    }
                    Row(modifier = Modifier.padding(bottom = 5.dp)) {
                        TextButton(
                            onClick = { onEvent(HabitEvent.DecFrequency(state.editFreq)) },
                            modifier = Modifier
                                .background(Color.DarkGray)
                                .size(w / 2, h)
                                .background(Color.Red)
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = ""
                            )
                        }
                        TextButton(
                            onClick = { onEvent(HabitEvent.IncFrequency(state.editFreq)) },
                            modifier = Modifier
                                .background(Color.DarkGray)
                                .size(w / 2, h)
                                .background(Color.Green)
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = ""
                            )
                        }
                        Button(
                            onClick = { /*TODO*/ },
                            enabled = false,
                            modifier = Modifier.padding(start = 5.dp)
                        ) {
                            Text(text = state.editFreq.toString())
                        }
                    }
                    // chose days
                    if (displayHabit.habitJoin.completion.occurrence.contains("0")) {
                        DaysSelection(onEvent, state)
                    }
                }
            }
        }
    }

}

@Composable
fun DaysSelection(onEvent: (HabitEvent) -> Unit, state: HabitState) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (index in 0 until 7) {
            val day = listOf("M", "T", "W", "T", "F", "S", "S")[index]
            val clicked =  state.editDays[index] == '1'
            DayButton(day, clicked, onEvent, index)
        }
    }
}

@Composable
fun DayButton(
    day: String,
    clicked: Boolean,
    onEvent: (HabitEvent) -> Unit,
    dayIndex: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = day)
        OutlinedButton(
            onClick = { onEvent(HabitEvent.UpDateEditDays(dayIndex, clicked)) },
            modifier = Modifier
                .size(width = 16.dp, height = 16.dp),
            border = BorderStroke(1.dp, Color.Black),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (clicked) Color.Black else Color.White,
            )
        ) {}
    }
}

@Composable
fun MoodSection(state: HabitState, onEvent: (MoodEvent) -> Unit, modifier: Modifier) {
    Column (
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "How do you feel?",
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 24.sp
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Bad",
                fontSize = 16.sp
            )

            // Mood radio buttons
            Row(verticalAlignment = Alignment.CenterVertically) {
                for (moodType in moods) {
                    RadioButton(
                        selected = state.selectedMood == moodType,
                        onClick = {
                            onEvent(
                                when (moodType) {
                                    MoodType.BAD -> MoodEvent.BadSelected(moodType)
                                    MoodType.SO_SO -> MoodEvent.SoSoSelected(moodType)
                                    MoodType.OK -> MoodEvent.OkSelected(moodType)
                                    MoodType.ALRIGHT -> MoodEvent.AlrightSelected(moodType)
                                    MoodType.GOOD -> MoodEvent.GoodSelected(moodType)
                                }
                            )
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(moodType.moodColor),
                            unselectedColor = Color.Gray,
                        ),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }

            Text(
                text = "Good",
                fontSize = 16.sp
            )
        }
    }
}
