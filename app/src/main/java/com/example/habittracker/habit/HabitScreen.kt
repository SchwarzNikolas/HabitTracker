package com.example.habittracker.habit

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import com.example.habittracker.database.HabitJoin

// details about compose available at https://developer.android.com/develop/ui/compose/layouts/basics

data class Item(
    val name: String,
    val onClick: () -> Unit,
    val icon: ImageVector
    )
@Composable
fun MainScreen (
    state: HabitState,
    onEvent: (HabitEvent) -> Unit
){
//    PopupBox(
//        popupWidth = 300f,
//        popupHeight = 300f,
//        showPopup = state.showEdit,
//        onClickOutside = { onEvent(HabitEvent.CancelEdit)},
//        content = { EditWindow(onEvent, state) }
//    )
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
            Text(text = state.date.toString())
            for (h in state.displayHabits){
                ElevatedHabit(displayHabit = h, onEvent = onEvent, habitJoin = h.habitJoin, state)
            }
            Button(onClick = { onEvent(HabitEvent.resetCompletion)}) {
                    Text(text = "test")
            }
        
            Text(text = "Weekly", textAlign = TextAlign.Center, modifier = Modifier
                .background(Color.Gray)
                .fillMaxWidth(), fontSize = 30.sp)
        
            Text(text = state.habitJoin.size.toString())


//            for (habit in state.habits) {
//                ElevatedHabit(habit, onEvent)
//                //Text(text = habit.done.value.toString())
//            }
            Text(text = state.habitRecord.size.toString())
            for (habitRecord in state.habitRecord) {
                Text(text = habitRecord.habitName)
                Text(text = habitRecord.date)
            }
        NumberPicker(value = state.editFreq, onValueChange = { onEvent(HabitEvent.UpDateEditFreq(it)) }, range = 1..10)
        }
    }

@Composable
fun ElevatedHabit(displayHabit: DisplayHabit, onEvent: (HabitEvent) -> Unit, habitJoin: HabitJoin, state: HabitState) {
    val dropdownItems :List<Item> = listOf(
        Item(name ="Edit", onClick =  {onEvent(HabitEvent.EditHabit(displayHabit))}, Icons.Default.Edit),
        Item(name = "Undo", onClick = {onEvent(HabitEvent.DecCompletion(habitJoin))}, Icons.Default.ArrowBack),
        Item(name = "Delete", onClick = {onEvent(HabitEvent.DeleteHabit(habitJoin))}, Icons.Default.Delete)
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
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(true) {
                    detectTapGestures(onLongPress = {
                        onEvent(HabitEvent.ContextMenuVisibility(displayHabit))
                        //pressoffser = DpOffset(it.x.toDp(), it.y.toDp())
                    })
                }
        ) {
            val focusManager = LocalFocusManager.current
            Column(horizontalAlignment = Alignment.Start) {
                if (displayHabit.beingEdited.value){
                    CustomTextField(
                        value = state.editString,
                        label = "",
                        onchange = { onEvent(HabitEvent.UpDateEditString(it)) },
                        manager = focusManager,
                    )
                }else {
                    TextField(
                        value = habitJoin.habit.name,
                        onValueChange = { },
                        label = { },
                        colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, cursorColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                        keyboardOptions = KeyboardOptions.Default.copy(autoCorrectEnabled = true,
                            imeAction = ImeAction.Done,
                            showKeyboardOnFocus = true,
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Text),
                        singleLine = true,
                        readOnly = true,
                        modifier = Modifier.size(height = 50.dp,width = 100.dp)
                        )
                }
                //Text(text = habitJoin.completion.completion.toString())
                //Text(text = habitJoin.habit.frequency.toString())
                Row() {
                    Box(modifier = Modifier.padding(start = 30.dp, bottom = 10.dp), contentAlignment = Alignment.Center) {
                        CircularProgressBar(angle = (habitJoin.completion.completion.toFloat() / habitJoin.habit.frequency) * 360)
                        Button(onClick = { onEvent(HabitEvent.IncCompletion(habitJoin))}, modifier = Modifier.size(100.dp), shape = CircleShape, colors = ButtonDefaults.buttonColors(
                        Color.Green), contentPadding = PaddingValues(0.dp)) {
                        if (habitJoin.completion.done){
                            Icon(imageVector =  Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(150.dp))

                        }else{
                                Text(text = habitJoin.completion.completion.toString(), modifier = Modifier.padding(end = 20.dp) )
                                Text(text = "/" ,modifier = Modifier.padding(start = 0.dp))
                                if (displayHabit.beingEdited.value){
                                    NumberPicker(value = state.editFreq, onValueChange = { onEvent(HabitEvent.UpDateEditFreq(it)) }, range = 1..10)

                                }
                                else {
                                    Text(
                                        text = habitJoin.habit.frequency.toString(),
                                        modifier = Modifier.padding(start = 20.dp)
                                    )
                                }

//                            Box {
//                                Text(text = habitJoin.completion.completion.toString() )
//                                Text(text = "-", fontSize = 30.sp)
//                                Text(text = habitJoin.habit.frequency.toString(), modifier = Modifier.padding(top = 28.dp))
//                            }
                        }
                        }
                    }
                    //                    for (n in 0..<displayHabit.habit.value.frequency) {
//                        HabitCheckBox(displayHabit, n, onEvent)
//                    }

                    if (displayHabit.beingEdited.value){
                        Button(onClick = { onEvent(HabitEvent.ModifyHabit)}) {
                            Text(text = "Edit")
                        }
                    }else{
                        IconButton(onClick = { onEvent(HabitEvent.ContextMenuVisibility(displayHabit))}){
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "ContextMenu")
                        }
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
                    onEvent(HabitEvent.ContextMenuVisibility(displayHabit)) }
                , leadingIcon = { Icon(imageVector = item.icon, contentDescription = null)})
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
            value = state.editFreq.toString(),
            label = "Frequency:",
            onchange = { onEvent(HabitEvent.UpDateEditFreq(it.toInt())) },
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
        colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent),
        keyboardOptions = KeyboardOptions.Default.copy(autoCorrectEnabled = true,
            imeAction = ImeAction.Done,
            showKeyboardOnFocus = true,
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text),
        keyboardActions = KeyboardActions(onDone = {
            manager.moveFocus(FocusDirection.Down) }),
        singleLine = true,
        modifier = Modifier.size(height = 50.dp,width = 100.dp)
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
                        .background(Color.Gray)
                        .clip(RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    content()
                }
            }
        }
    }
}

