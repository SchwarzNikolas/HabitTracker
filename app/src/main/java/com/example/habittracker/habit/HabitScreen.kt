package com.example.habittracker.habit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            //.verticalScroll(rememberScrollState())
            //.fillMaxWidth(),
        ,horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        Text(text = state.date.toString())

        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 158.dp)) {

            items(state.displayHabits.size){
                    ElevatedHabit(
                        displayHabit = state.displayHabits[it],
                        onEvent = onEvent,
                        habitJoin = state.displayHabits[it].habitJoin,
                        state
                    )
            }
        }
            Button(onClick = { onEvent(HabitEvent.resetCompletion)}) {
                    Text(text = "test")
            }
        Button(onClick = { onEvent(HabitEvent.nextDay)}) {
            Text(text = "next day")
        }
        Text(text = state.day.toString())
        Text(text = state.job.isActive.toString())
        state.job.children.forEach {
            Text(text = it.isActive.toString())
        }
            Text(text = "Weekly", textAlign = TextAlign.Center, modifier = Modifier
                .background(Color.Gray)
                .fillMaxWidth(), fontSize = 30.sp)

        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 158.dp)) {

            items(state.weeklyDisplayHabits.size){
                ElevatedHabit(
                    displayHabit = state.weeklyDisplayHabits[it],
                    onEvent = onEvent,
                    habitJoin = state.weeklyDisplayHabits[it].habitJoin,
                    state
                )
            }
        }
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
        //NumberPicker(value = state.editFreq, onValueChange = { onEvent(HabitEvent.UpDateEditFreq(it)) }, range = 1..10)
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
            modifier = Modifier,
            //.fillMaxWidth()
//                .pointerInput(true) {
//                    detectTapGestures(onLongPress = {
//                        onEvent(HabitEvent.ContextMenuVisibility(displayHabit))
//                        //pressoffser = DpOffset(it.x.toDp(), it.y.toDp())
            //})
            //},
            contentAlignment = Alignment.Center
        ) {
            val focusManager = LocalFocusManager.current
            Column {


                if (displayHabit.beingEdited.value) {
                    EditMode(
                        state = state,
                        onEvent = onEvent,
                        habitJoin = habitJoin,
                        focusManager = focusManager
                    )
                } else {
                    DisplayMode(
                        displayHabit = displayHabit,
                        onEvent = onEvent,
                        habitJoin = habitJoin
                    )
                }

//                if (displayHabit.beingEdited.value){
//                    CustomTextField(
//                        value = state.editString,
//                        label = "",
//                        onchange = { onEvent(HabitEvent.UpDateEditString(it)) },
//                        manager = focusManager,
//                    )
//                }else {
//                    TextField(
//                        value = habitJoin.habit.name,
//                        onValueChange = { },
//                        label = { },
//                        colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, cursorColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
//                        keyboardOptions = KeyboardOptions.Default.copy(autoCorrectEnabled = true,
//                            imeAction = ImeAction.Done,
//                            showKeyboardOnFocus = true,
//                            capitalization = KeyboardCapitalization.Sentences,
//                            keyboardType = KeyboardType.Text),
//                        singleLine = true,
//                        readOnly = true,
//                        modifier = Modifier.size(height = 50.dp,width = 100.dp)
//                        )
//                }
//                //Text(text = habitJoin.completion.completion.toString())
//                //Text(text = habitJoin.habit.frequency.toString())
//                Row() {
//                    Box(modifier = Modifier.padding(start = 30.dp, bottom = 10.dp), contentAlignment = Alignment.Center) {
//                        CircularProgressBar(angle = (habitJoin.completion.completion.toFloat() / habitJoin.habit.frequency) * 360)
//                        Button(onClick = { onEvent(HabitEvent.IncCompletion(habitJoin))}, modifier = Modifier.size(100.dp), shape = CircleShape, colors = ButtonDefaults.buttonColors(
//                        Color.Green), contentPadding = PaddingValues(0.dp)) {
//                        if (habitJoin.completion.done){
//                            Icon(imageVector =  Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(150.dp))
//
//                        }else{
//                                Text(text = habitJoin.completion.completion.toString(), modifier = Modifier.padding(end = 20.dp) )
//                                Text(text = "/" ,modifier = Modifier.padding(start = 0.dp))
//                                if (displayHabit.beingEdited.value){
//                                    NumberPicker(value = state.editFreq, onValueChange = { onEvent(HabitEvent.UpDateEditFreq(it)) }, range = 1..10)
//
//                                }
//                                else {
//                                    Text(
//                                        text = habitJoin.habit.frequency.toString(),
//                                        modifier = Modifier.padding(start = 20.dp)
//                                    )
//                                }
//
////                            Box {
////                                Text(text = habitJoin.completion.completion.toString() )
////                                Text(text = "-", fontSize = 30.sp)
////                                Text(text = habitJoin.habit.frequency.toString(), modifier = Modifier.padding(top = 28.dp))
////                            }
//                        }
//                        }
//                    }
//                    //                    for (n in 0..<displayHabit.habit.value.frequency) {
////                        HabitCheckBox(displayHabit, n, onEvent)
////                    }
//
//                    if (displayHabit.beingEdited.value){
//                        Button(onClick = { onEvent(HabitEvent.ModifyHabit)}) {
//                            Text(text = "Edit")
//                        }
//                    }else{
//                        IconButton(onClick = { onEvent(HabitEvent.ContextMenuVisibility(displayHabit))}){
//                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "ContextMenu")
//                        }
//                    }
//                }

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


@Composable
fun DisplayMode(displayHabit: DisplayHabit, onEvent: (HabitEvent) -> Unit, habitJoin: HabitJoin){


    BasicTextField(state = TextFieldState(habitJoin.habit.name),

//            colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, cursorColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
            keyboardOptions = KeyboardOptions.Default.copy(autoCorrectEnabled = true,
                imeAction = ImeAction.Done,
                showKeyboardOnFocus = true,
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text),
            lineLimits = androidx.compose.foundation.text.input.TextFieldLineLimits.SingleLine,
            readOnly = true,
            //modifier = Modifier.size(height = 75.dp,width = 100.dp),
            textStyle = LocalTextStyle.current.copy(fontSize = 30.sp),
            //shape = CircleShape,
            //modifier = Modifier.padding(0.dp)
        )

    //Text(text = habitJoin.completion.completion.toString())
    //Text(text = habitJoin.habit.frequency.toString())
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



@Composable
fun EditMode(state: HabitState, onEvent: (HabitEvent) -> Unit, habitJoin: HabitJoin, focusManager: FocusManager) {
//    CustomTextField(
//        value = state.editString,
//        label = "",
//        onchange = { onEvent(HabitEvent.UpDateEditString(it)) },
//        manager = focusManager,
//    )
    BasicTextField(state = TextFieldState(habitJoin.habit.name),
//            value = habitJoin.habit.name,
//            onValueChange = { },
//            label = { },
//            colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, cursorColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
        keyboardOptions = KeyboardOptions.Default.copy(autoCorrectEnabled = true,
            imeAction = ImeAction.Done,
            showKeyboardOnFocus = true,
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text),
        lineLimits = androidx.compose.foundation.text.input.TextFieldLineLimits.SingleLine,
        //singleLine = true,
        //readOnly = true,
        //modifier = Modifier.size(height = 75.dp,width = 100.dp),
        textStyle = LocalTextStyle.current.copy(fontSize = 30.sp),
        //shape = CircleShape,
        modifier = Modifier.padding(0.dp),

    )
    Row {
        Box(
            modifier = Modifier.padding(start = 30.dp, bottom = 10.dp),
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
                    modifier = Modifier.padding(end = 20.dp)
                )
                Text(text = "/", modifier = Modifier.padding(start = 0.dp))

                NumberPicker(
                    value = state.editFreq,
                    onValueChange = { onEvent(HabitEvent.UpDateEditFreq(it)) },
                    range = 1..10
                )

//                            Box {
//                                Text(text = habitJoin.completion.completion.toString() )
//                                Text(text = "-", fontSize = 30.sp)
//                                Text(text = habitJoin.habit.frequency.toString(), modifier = Modifier.padding(top = 28.dp))
//                            }
            }
        }
        IconButton(onClick = { onEvent(HabitEvent.ModifyHabit)}){
            Icon(imageVector = Icons.Default.Check, contentDescription = "ContextMenu")

        }
//        Button(onClick = { onEvent(HabitEvent.ModifyHabit) }) {
//            Text(text = "Edit")
//        }
    }
    //                    for (n in 0..<displayHabit.habit.value.frequency) {
//                        HabitCheckBox(displayHabit, n, onEvent)
//                    }
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

//@Composable
//fun EditWindow(onEvent: (HabitEvent) -> Unit, state: HabitState){
//    val focusManager = LocalFocusManager.current
//    Column {
//        CustomTextField(
//            value = state.editString,
//            label = "Name:",
//            onchange = { onEvent(HabitEvent.UpDateEditString(it)) },
//            manager = focusManager,
//        )
//        CustomTextField(
//            value = state.editFreq.toString(),
//            label = "Frequency:",
//            onchange = { onEvent(HabitEvent.UpDateEditFreq(it.toInt())) },
//            manager = focusManager,
//            )
//        Button(onClick = { onEvent(HabitEvent.ModifyHabit) }) {
//            Text(text = "Execute edit")
//        }
//    }
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
//
//@Composable
//fun PopupBox(popupWidth: Float, popupHeight:Float, showPopup:Boolean, onClickOutside: ()-> Unit, content: @Composable () -> Unit){
//    if (showPopup) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .zIndex(10f),
//            contentAlignment = Alignment.Center
//        ) {
//            Popup(
//                alignment = Alignment.Center,
//                properties = PopupProperties(
//                    excludeFromSystemGesture = true,
//                    focusable = true
//                ),
//                onDismissRequest = { onClickOutside() },
//            ) {
//                Box(
//                    Modifier
//                        .width(popupWidth.dp)
//                        .height(popupHeight.dp)
//                        .background(Color.Gray)
//                        .clip(RoundedCornerShape(4.dp)),
//                    contentAlignment = Alignment.Center
//                ) {
//                    content()
//                }
//            }
//        }
//    }
//}

