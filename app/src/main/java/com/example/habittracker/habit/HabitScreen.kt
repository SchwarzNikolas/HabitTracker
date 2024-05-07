package com.example.habittracker.habit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habittracker.custom.CustomHabitEvent
import com.example.habittracker.mood.MoodEvent
import com.example.habittracker.mood.MoodState
import com.example.habittracker.mood.MoodType


// details about compose available at https://developer.android.com/develop/ui/compose/layouts/basics

data class Item(
    val name: String,
    val onClick: () -> Unit,
    val icon: ImageVector
    )
@Composable
fun MainScreen (
    state: HabitState,
    moodState: MoodState,
    onEvent: (HabitEvent) -> Unit,
    onMoodEvent: (MoodEvent) -> Unit
){
    //val scrollState = rememberScrollState()
    Column(
        modifier = Modifier,
        //.verticalScroll(state = scrollState)
        //.verticalScroll(rememberScrollState())
        //.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        Text(text = state.date.toString())

        // Mood part of the screen
        MoodSection(moodState, onMoodEvent, Modifier)

        // Daily part of the screen
        Text(text = "Daily", textAlign = TextAlign.Center, modifier = Modifier
            .background(Color.Gray)
            .fillMaxWidth(), fontSize = 30.sp)

        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 158.dp)) {
            items(state.displayHabits.size){
                ElevatedHabit(
                    displayHabit = state.displayHabits[it],
                    onEvent = onEvent,
                    state
                )
            }
            item(span = {GridItemSpan(2)}) {
                Text(text = "Weekly", textAlign = TextAlign.Center, modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxWidth(), fontSize = 30.sp)
            }
            items(state.weeklyDisplayHabits.size){
                ElevatedHabit(
                    displayHabit = state.weeklyDisplayHabits[it],
                    onEvent = onEvent,
                    state
                )
            }
        }

        Button(onClick = { onEvent(HabitEvent.ResetCompletion)}) {
                Text(text = "test")
        }
        Button(onClick = { onEvent(HabitEvent.NextDay)}) {
            Text(text = "next day")
        }
        Text(text = state.date.dayOfWeek.toString())
        Text(text = state.date2.dayOfWeek.toString())

        // Weekly part of the screen
//        Text(text = "Weekly", textAlign = TextAlign.Center, modifier = Modifier
//            .background(Color.Gray)
//            .fillMaxWidth(), fontSize = 30.sp)

//        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 158.dp)) {
//            items(state.weeklyDisplayHabits.size){
//                ElevatedHabit(
//                    displayHabit = state.weeklyDisplayHabits[it],
//                    onEvent = onEvent,
//                    state
//                )
//            }
//        }
        Text(text = state.habitRecord.size.toString())
        for (habitRecord in state.habitRecord) {
            Text(text = habitRecord.habitName)
            Text(text = habitRecord.date)
        }
    }
}

@Composable
fun ElevatedHabit(displayHabit: DisplayHabit, onEvent: (HabitEvent) -> Unit, state: HabitState) {
    val dropdownItems :List<Item> = listOf(
        Item(name ="Edit", onClick =  {onEvent(HabitEvent.EditHabit(displayHabit))}, Icons.Default.Edit),
        Item(name = "Undo", onClick = {onEvent(HabitEvent.DecCompletion(displayHabit.habitJoin))}, Icons.Default.ArrowBack),
        Item(name = "Delete", onClick = {onEvent(HabitEvent.DeleteHabit(displayHabit.habitJoin))}, Icons.Default.Delete)
       )

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            //.size(width = 380.dp, height = 130.dp)
            .padding(vertical = 5.dp, horizontal = 5.dp),
        colors = CardDefaults.cardColors(Color.Gray)

    ) {
        Box(
            modifier = Modifier,
//            .fillMaxWidth()
//                .pointerInput(true) {
//                    detectTapGestures(onLongPress = {
//                        onEvent(HabitEvent.ContextMenuVisibility(displayHabit))
//                        pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
//            })
//            },
            contentAlignment = Alignment.Center
        ) {
            Column {
                if (displayHabit.beingEdited.value) {
                    EditMode(
                        onEvent = onEvent,
                        displayHabit = displayHabit,
                        state = state,
                    )
                } else {
                    DisplayMode(
                        displayHabit = displayHabit,
                        onEvent = onEvent,
                    )
                }
            }
        }
    }
    DropdownMenu(
        expanded = displayHabit.isMenuVisible.value,
        onDismissRequest = { onEvent(HabitEvent.ContextMenuVisibility(displayHabit))}
    ) {
        dropdownItems.forEach { item ->
            DropdownMenuItem(
                text = { Text(text = item.name)},
                onClick = {
                    item.onClick()
                    onEvent(HabitEvent.ContextMenuVisibility(displayHabit))},
                leadingIcon = { Icon(imageVector = item.icon, contentDescription = null)}
            )
        }
    }
}


@Composable
fun DisplayMode(onEvent: (HabitEvent) -> Unit, displayHabit: DisplayHabit){

    val habitJoin = displayHabit.habitJoin

    BasicTextField(
        value = displayHabit.habitJoin.habit.name,
        onValueChange = { },
        keyboardOptions = KeyboardOptions.Default.copy(
            autoCorrectEnabled = true,
            imeAction = ImeAction.Done,
            showKeyboardOnFocus = true,
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text
        ),
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(fontSize = 30.sp),
        modifier = Modifier.padding(bottom = 5.dp),
        readOnly = true,
    )
    Row {
        Box(modifier = Modifier.padding(start = 30.dp, bottom = 10.dp), contentAlignment = Alignment.Center) {
            CircularProgressBar(angle = (habitJoin.completion.completion.toFloat() / habitJoin.habit.frequency) * 360)
            Button(onClick = { onEvent(HabitEvent.IncCompletion(habitJoin))}, modifier = Modifier.size(100.dp), shape = CircleShape, colors = ButtonDefaults.buttonColors(
                Color.Green), contentPadding = PaddingValues(0.dp)) {
                if (habitJoin.completion.done){
                    Icon(imageVector =  Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(150.dp))

                }else{
                    Text(text = habitJoin.completion.completion.toString(), modifier = Modifier.padding(end = 20.dp),fontSize = 20.sp )
                    Text(text = "/" ,modifier = Modifier.padding(start = 0.dp),fontSize = 20.sp)
                    Text(
                        text = habitJoin.habit.frequency.toString(),
                        modifier = Modifier.padding(start = 20.dp),
                        fontSize = 20.sp
                    )
                }
            }
        }
        IconButton(onClick = { onEvent(HabitEvent.ContextMenuVisibility(displayHabit))}){
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "ContextMenu")
        }
    }
}


@Composable
fun EditMode(onEvent: (HabitEvent) -> Unit, displayHabit: DisplayHabit, state: HabitState) {
    BasicTextField(value = state.editString,
        onValueChange = { onEvent(HabitEvent.UpDateEditString(it))},
        keyboardOptions = KeyboardOptions.Default.copy(autoCorrectEnabled = true,
            imeAction = ImeAction.Done,
            showKeyboardOnFocus = true,
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text),
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(fontSize = 30.sp,
            textDecoration = TextDecoration.Underline),
        modifier = Modifier.padding(bottom = 5.dp)
    )

    val habitJoin = displayHabit.habitJoin
    Row {
        Box(
            modifier = Modifier
                .padding(start = 30.dp, bottom = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressBar(angle = (habitJoin.completion.completion.toFloat() / habitJoin.habit.frequency) * 360)
            Button(
                onClick = { onEvent(HabitEvent.IncCompletion(habitJoin)) },
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    Color.Green,
                    disabledContainerColor = Color.Green,
                    disabledContentColor = Color.White
                ),
                contentPadding = PaddingValues(0.dp),
                enabled = false
            ) {
                Text(
                    text = habitJoin.completion.completion.toString(),
                    modifier = Modifier
                        .padding(end = 20.dp),
                    fontSize = 20.sp
                )
                Text(
                    text = "/" ,
                    modifier = Modifier
                        .padding(start = 0.dp),
                    fontSize = 20.sp
                )
                Box(
                    modifier = Modifier.
                    padding(start = 20.dp)
                ) {
                    NumberPicker(
                        value = state.editFreq,
                        onValueChange = { onEvent(HabitEvent.UpDateEditFreq(it)) },
                        range = 1..10
                    )
                }
            }
        }

        Column {
            IconButton(
                onClick = { onEvent(HabitEvent.ModifyHabit(habitJoin))}
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "ContextMenu")
            }
            IconButton(onClick = { onEvent(HabitEvent.CancelEdit(displayHabit))}
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "cancel edit")
            }
        }
    }

    // chose days
    if (habitJoin.completion.occurrence.contains("0")) {
        DaysSelection(onEvent, displayHabit, state)
    }
}
//@Composable
//fun HabitCheckBox(displayHabit: DisplayHabit, index:Int, onEvent: (HabitEvent) -> Unit){
//    Checkbox(
//        checked = displayHabit.completion[index].value,
//        onCheckedChange = {onEvent(HabitEvent.BoxChecked(displayHabit, index))},
//        modifier = Modifier.padding(5.dp),
//        colors = CheckboxDefaults.colors(Color.Green),
//    )
//}

@Composable
fun CustomTextField(value: String, label: String, onchange: (String) -> Unit, manager: FocusManager){
    TextField(
        value = value,
        onValueChange = { onchange(it) },
        label = { Text(label) },
        colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent),
        keyboardOptions = KeyboardOptions.Default.copy(autoCorrectEnabled = true,
            imeAction = ImeAction.Done,
            showKeyboardOnFocus = true,
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text),
        keyboardActions = KeyboardActions(onDone = {
            manager.moveFocus(FocusDirection.Down) }),
        singleLine = true,
        modifier = Modifier.size(height = 50.dp,width = 100.dp),
        shape = CircleShape
    )
}

@Composable
fun DaysSelection(onEvent: (HabitEvent) -> Unit, displayHabit: DisplayHabit, state: HabitState) {
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
fun MoodSection(moodState: MoodState, onMoodEvent: (MoodEvent) -> Unit, modifier: Modifier) {
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
                for (moodType in moodState.moods) {
                    RadioButton(
                        selected = moodState.selectedMood == moodType,
                        onClick = {
                            onMoodEvent(
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