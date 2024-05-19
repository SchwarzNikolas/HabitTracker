package com.habittracker.rootreflect.habit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
<<<<<<< HEAD
import androidx.compose.ui.focus.focusProperties
=======
>>>>>>> ea9c95543c5ab83d255bd33960c6ef7d04f7d36d
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


// details about compose available at https://developer.android.com/develop/ui/compose/layouts/basics

data class Item(
    val name: String,
    val onClick: (DisplayHabit) -> Unit,
    val icon: ImageVector
    )
private val moods: List<MoodType> = enumValues<MoodType>().toList()


@Composable
fun MainScreen (
    state: HabitState,
    onEvent: (HabitEvent) -> Unit,

) {
    val dropdownItems: List<Item> = listOf(
        Item(
            name = "Edit",
            onClick = { displayHabit: DisplayHabit -> onEvent(HabitEvent.EditHabit(displayHabit)) },
            Icons.Default.Edit
        ),
        Item(
            name = "Undo",
            onClick = { displayHabit: DisplayHabit -> onEvent(HabitEvent.DecCompletion(displayHabit.habit)) },
            Icons.Default.ArrowBack
        ),
        Item(
            name = "Delete",
            onClick = { displayHabit: DisplayHabit -> onEvent(HabitEvent.DeleteHabit(displayHabit.habit)) },
            Icons.Default.Delete
        )
    )

    Column {
        // Mood part of the screen
        Box {
            MoodSection(state, Modifier, onEvent)
            NotificationBox(visible = state.showNotification,
                action =  {onEvent(HabitEvent.ToggleNotificationVisibility)},
                text =  state.editString)
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .width(4.dp)
        )
        val listState = rememberLazyListState()

        LazyColumn(state = listState, modifier = Modifier) {
            if (state.fixScroll.value) {
                listState.requestScrollToItem(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset)
                onEvent(HabitEvent.ToggleScroll)
            }

                items(
                count = state.displayHabits.size,
                contentType = { index -> state.displayHabits[index] },
                key = { index -> state.displayHabits[index].habit.habitId },

                ) {
                HabitDisplay(
                    onEvent, state, state.displayHabits[it], dropdownItems,
                    Modifier.animateItem(
                        placementSpec = spring(
                            stiffness = Spring.StiffnessMediumLow,
                        )
                    )
                )
            }

        }
    }
}



@Composable
fun HabitDisplay(onEvent: (HabitEvent) -> Unit, state: HabitState, displayHabit: DisplayHabit, dropdownItems: List<Item>, modifier: Modifier = Modifier){
    val mod = Modifier
    if (displayHabit.beingEdited.value) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(modifier = mod.weight(1f),
                onClick = { onEvent(HabitEvent.ModifyHabit(displayHabit)) }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "ContextMenu"
                )
            }
            EditMode(
                onEvent = onEvent,
                displayHabit = displayHabit,
                state = state,
                modifier = mod.weight(12f)
            )
            IconButton(modifier = mod.weight(1f),
                onClick = { onEvent(HabitEvent.CancelEdit(displayHabit)) }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "cancel edit"
                )
            }
        }
    } else {
        DisplayMode(
            onEvent = onEvent,
            displayHabit = displayHabit,
            dropdownItems = dropdownItems,
            modifier = modifier
        )
    }
}

@Composable
fun DisplayMode(onEvent: (HabitEvent) -> Unit, displayHabit: DisplayHabit, dropdownItems: List<Item> , modifier: Modifier = Modifier){
    val imageAlpha : Float by animateFloatAsState(targetValue = if(displayHabit.habit.done ){0.5f} else {1f})
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .alpha(imageAlpha)
            .clickable {
                onEvent(HabitEvent.IncCompletion(displayHabit.habit))
            },
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)

    ) {
        Row {
            BasicText(
                text = displayHabit.habit.name,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(17f),
                style = LocalTextStyle.current.copy(
                    fontSize = 30.sp,
                    lineHeight = 30.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                ),
            )
            IconButton(
                onClick = { onEvent(HabitEvent.ContextMenuVisibility(displayHabit)) },
                modifier = Modifier.weight(2f)
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
                                item.onClick(displayHabit)
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
        Row(modifier = Modifier.padding(bottom = 5.dp, start = 10.dp, end = 5.dp),
            verticalAlignment = Alignment.CenterVertically) {

            BoxWithConstraints (modifier = Modifier
                .weight(6f)
                .height(50.dp)){
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .size(width = maxWidth, height = maxHeight)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if(displayHabit.habit.occurrence == "1111111"){
                                MaterialTheme.colorScheme.onSecondaryContainer
                            } else {
                                MaterialTheme.colorScheme.onTertiaryContainer
                            })
                        .animateContentSize(finishedListener = { x, y ->
                            run {
                                onEvent(HabitEvent.CheckCompletion(displayHabit.habit))

                            }
                        }
                        )
                        .size(
                            maxWidth * (displayHabit.habit.completion.toFloat() / displayHabit.habit.frequency),
                            maxHeight
                        )
                )
            }
            Box(modifier = Modifier
                .padding(start = 10.dp, end = 5.dp)
                .clip(RoundedCornerShape(10.dp))
                .weight(1f)
                .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,) {
                Text(modifier = Modifier.padding(bottom = 2.dp),
                    text = displayHabit.habit.frequency.toString(),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}


@Composable
fun EditMode(onEvent: (HabitEvent) -> Unit, displayHabit: DisplayHabit, state: HabitState, modifier: Modifier = Modifier) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
            .padding(vertical = 5.dp, horizontal = 5.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
    ) {
        Row {
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
                maxLines = 4,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 30.sp,
                    textDecoration = TextDecoration.Underline,
                    lineHeight = 30.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = modifier.padding(start = 10.dp),
            )
        }
        Row(modifier = Modifier.padding(bottom = 8.dp, end = 5.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Slider(
                value = state.editFreq.toFloat(),
                onValueChange = { onEvent(HabitEvent.UpDateEditFreq(it.toInt()))
                },
                valueRange = 1f..9f,
                steps = 7,
                modifier = Modifier
                    .weight(6f)
                    .height(50.dp)
                    .padding(start = 5.dp),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.onPrimary,
                    activeTrackColor = if (displayHabit.habit.occurrence == "1111111") {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        } else {
                            MaterialTheme.colorScheme.onTertiaryContainer
                        },
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    activeTickColor = MaterialTheme.colorScheme.onPrimary,
                    inactiveTickColor = MaterialTheme.colorScheme.tertiary
                )
            )
            Box(modifier = Modifier
                .padding(start = 10.dp, end = 5.dp)
                .clip(RoundedCornerShape(10.dp))
                .weight(1f)
                .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,) {
                Text(modifier = Modifier.padding(bottom = 2.dp),
                    text = state.editFreq.toString(),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
        // chose days
        if (displayHabit.habit.occurrence.contains("0")) {
            DaysSelection(onEvent, state)
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
        ){}
    }
}

@Composable
fun MoodSection(state: HabitState, modifier: Modifier, event : (HabitEvent)-> Unit) {
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
                            event(HabitEvent.MoodSelected(moodType))
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

@Composable
fun NotificationBox(visible : Boolean, action: () -> Unit, text: String){
    AnimatedVisibility(visible = visible,
        enter = slideInHorizontally { fullWidth: Int ->  -fullWidth},
        exit = fadeOut()) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 5.dp),
            colors = CardDefaults.cardColors(Color.Gray)
        ){
            Box(modifier = Modifier.padding(5.dp)) {
                Text(text = text)
            }
    }
}
    if (visible){
        action()
    }

}
