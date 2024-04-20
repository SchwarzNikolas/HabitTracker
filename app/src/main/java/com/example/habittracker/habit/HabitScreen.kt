package com.example.habittracker.habit

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex

// details about compose available at https://developer.android.com/develop/ui/compose/layouts/basics

data class Item(
    val name: String,
    val onClick: () -> Unit
    )
@Composable
fun MainScreen (
    state: HabitState,
    onEvent: (HabitEvent) -> Unit
){
    PopupBox(
        popupWidth = 300f,
        popupHeight = 300f,
        showPopup = state.showEdit,
        onClickOutside = { onEvent(HabitEvent.CancelEdit)},
        content = { EditWindow(onEvent, state) }
    )
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
            for (habit in state.habits) {
                ElevatedHabit(habit, onEvent)
                //Text(text = habit.done.value.toString())
            }
            Text(text = state.habitRecord.size.toString())
            for (habitRecord in state.habitRecord) {
                Text(text = habitRecord.habitName)
                Text(text = habitRecord.date)
            }
        }
    }

@Composable
fun ElevatedHabit(displayHabit: DisplayHabit, onEvent: (HabitEvent) -> Unit) {
    val dropdownItems :List<Item> = listOf(
        Item(name ="edit", onClick =  {onEvent(HabitEvent.EditHabit(displayHabit))}),
        Item(name = "delete", onClick = {onEvent(HabitEvent.DeleteHabit(displayHabit))})
    )
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 380.dp, height = 130.dp)
            .padding(vertical = 5.dp),
        colors = CardDefaults.cardColors(Color.Green)

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(true) {
                    detectTapGestures(onLongPress = {
                        onEvent(HabitEvent.ContextMenuVisibility(displayHabit))
                        //pressoffser = DpOffset(it.x.toDp(), it.y.toDp())
                    })
                }
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = displayHabit.habit.value.name,
                    modifier = Modifier
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
                Row(modifier = Modifier.size(width = 380.dp, height = 30.dp)) {
                    for (n in 0..<displayHabit.habit.value.frequency) {
                        HabitCheckBox(displayHabit, n, onEvent)
                    }
                    IconButton(onClick = { onEvent(HabitEvent.ContextMenuVisibility(displayHabit))}){
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "ContextMenu")
                    }
                }

//                Row {
////                    Button(onClick = { onEvent(HabitEvent.EditHabit(displayHabit)) }) {
////                        Text(text = "edit")
////                    }
////                    Button(onClick = { onEvent(HabitEvent.DeleteHabit(displayHabit)) }) {
////                        Text(text = "delete")
////                    }
////                    Button(onClick = { onEvent(HabitEvent.ContextMenuVisibility(displayHabit))}) {
////                        Text(text = "...")
////                    }
//                }
            }
        }
        DropdownMenu(expanded = displayHabit.isMenuVisible.value, onDismissRequest = { onEvent(HabitEvent.ContextMenuVisibility(displayHabit))}) {
            dropdownItems.forEach { item ->
                DropdownMenuItem(text = { Text(text = item.name)}, onClick = {
                    item.onClick()
                    onEvent(HabitEvent.ContextMenuVisibility(displayHabit)) })
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
        colors = CheckboxDefaults.colors(Color.Green),
    )
}

@Composable
fun EditWindow(onEvent: (HabitEvent) -> Unit, state: HabitState){
    val focusManager = LocalFocusManager.current
    Column {
        CustomTextField(
            value = state.editString,
            label = "Name:",
            onchange = { onEvent(HabitEvent.UpDateEditString(it)) },
            manager = focusManager,
        )
        CustomTextField(
            value = state.editFreq,
            label = "Frequency:",
            onchange = { onEvent(HabitEvent.UpDateEditFreq(it)) },
            manager = focusManager,
            )
        Button(onClick = { onEvent(HabitEvent.ModifyHabit) }) {
            Text(text = "Execute edit")
        }
    }
}

@Composable
fun CustomTextField(value: String, label: String, onchange: (String) -> Unit, manager: FocusManager){
    TextField(
        value = value,
        onValueChange = { onchange(it) },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(autoCorrectEnabled = true,
            imeAction = ImeAction.Done,
            showKeyboardOnFocus = true,
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text),
        keyboardActions = KeyboardActions(onDone = {
            manager.moveFocus(FocusDirection.Down) }),
        singleLine = true,

    )
}

@Composable
fun PopupBox(popupWidth: Float, popupHeight:Float, showPopup:Boolean, onClickOutside: ()-> Unit, content: @Composable () -> Unit){
    if (showPopup) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(10f),
            contentAlignment = Alignment.Center
        ) {
            Popup(
                alignment = Alignment.Center,
                properties = PopupProperties(
                    excludeFromSystemGesture = true,
                    focusable = true
                ),
                onDismissRequest = { onClickOutside() },
            ) {
                Box(
                    Modifier
                        .width(popupWidth.dp)
                        .height(popupHeight.dp)
                        .background(Color.Blue)
                        .clip(RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    content()
                }
            }
        }
    }
}

