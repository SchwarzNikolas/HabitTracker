package com.habittracker.rootreflect.history

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.habittracker.rootreflect.database.HabitRecord
import java.io.IOException
import java.time.LocalDate
import java.time.Month


@Composable
fun HistoryScreen(
    state: HistoryState,
    onEvent: (HistoryEvent) -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = "Select a Plant to display a Habit",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        Box {
            val flowerFrequency: List<@Composable (HabitRecord) -> Unit> = listOf(
                {FlowerClick(onEvent = onEvent, state = state, flower = "images/blueflower.png", offsetX = 255.dp, offsetY = 295.dp, habitRecord = it)},
                {FlowerClick(onEvent = onEvent, state = state, flower = "images/orangeflower.png", offsetX = 100.dp, offsetY = 275.dp, habitRecord = it)},
                {FlowerClick(onEvent = onEvent, state = state, flower = "images/pinkflower.png", offsetX = 135.dp, offsetY = 300.dp, habitRecord = it)},
                {FlowerClick(onEvent = onEvent, state = state, flower = "images/yelloworangeflower.png", offsetX = 320.dp, offsetY = 310.dp, habitRecord = it)},
                {FlowerClick(onEvent = onEvent, state = state, flower = "images/blueflower.png", offsetX = 80.dp, offsetY = 320.dp, habitRecord = it)},
                {FlowerClick(onEvent = onEvent, state = state, flower = "images/yelloworangeflower.png", offsetX = 190.dp, offsetY = 315.dp, habitRecord = it)})


            val bushFrequency: List<@Composable (HabitRecord) -> Unit> = listOf(
                {BushClick(onEvent = onEvent, state = state, bush = "images/bush1.png", offsetX = 50.dp, offsetY = 200.dp, habitRecord = it )}, // do again with bush random return val
                {BushClick(onEvent = onEvent, state = state, bush = "images/bush2.png", offsetX = 160.dp, offsetY = 250.dp, habitRecord = it)},
                {BushClick(onEvent = onEvent, state = state, bush = "images/bush3.png", offsetX = 260.dp, offsetY = 220.dp, habitRecord = it)})

            Background()
            SunMary(onEvent = onEvent)

            for(i in flowerFrequency.indices) {
                 if (state.habitListF1.size > i){
                     flowerFrequency[i](state.habitListF1[i])
                 }
            }
            for(i in bushFrequency.indices) {
                if (state.habitListF2.size > i){
                    bushFrequency[i](state.habitListF2[i])
                }
            }
            if (state.habitListF3Above.isNotEmpty()){
                TreeClick(onEvent = onEvent, state = state, habitRecord = state.habitListF3Above[0])
            }
            NameTag(show = state.nameTagActive, showe = state.offset, onEvent, state)

        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Select a Day to display your daily summary",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.padding(10.dp)){
            DateText(state = state)
        }
        Row(modifier = Modifier.height(165.dp)) {
            MonthSelector(onEvent = onEvent, state = state)
            MonthlyHistory(onEvent = onEvent, state = state)
        }
        if (state.bottomSheetActive){
            InfoSheet(onEvent = onEvent, state = state)
        }
    }
}

@Composable
fun DateText(state: HistoryState){
    /*
    Text which displays the year and the day
     */
    Spacer(modifier = Modifier.width(48.dp))
    Text(text = state.selectedYear.toString(),
        modifier = Modifier.width(112.dp))
    val days = listOf("M", "T", "W", "T", "F", "S", "S")
    LazyVerticalGrid(
        columns = GridCells.Fixed(7)){
        items(days.size){
            Text(text = days[it], textAlign = TextAlign.Center)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoSheet(onEvent: (HistoryEvent) -> Unit, state: HistoryState){
    /*
    Bottom sheet which will display additional information either about the selected day
    or about specific habits
     */
    ModalBottomSheet(
        onDismissRequest = {
            onEvent(HistoryEvent.DisableBottomSheet)
        },
    ) {
        Box(modifier = Modifier
            .height(state.infoCardHeight)
            .align(Alignment.CenterHorizontally)
            .padding(10.dp)){
            if (state.habitInfo) {
                Column {
                    Text(text = "Good job! You completed: " + (state.habitStored?.habitName))

                }
                // display information about habits

            }
            else {
                Column {
                    if (state.selectedMood != "No mood") {
                        Text(
                            text = "On the " + state.selectedDate.dayOfMonth.toString() + " of "
                                    + state.selectedDate.month?.name?.substring(0, 1)
                                    + state.selectedDate.month?.name?.substring(1)?.lowercase()
                                    + " " + state.selectedYear.toString()
                                    + " you felt " + state.selectedMood.lowercase() + ".",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    else {
                        Text(
                            text = "On the " + state.selectedDate.dayOfMonth.toString() + " of "
                                + state.selectedDate.month?.name?.substring(0, 1)
                                + state.selectedDate.month?.name?.substring(1)?.lowercase()
                                + " " + state.selectedYear.toString()
                                + " you logged no mood.",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth())
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp))
                    Spacer(modifier = Modifier.height(10.dp))

                    if (state.habitList.isEmpty()){
                        Text(text = "You have not completed any habits on that day.",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth())
                    }
                    else{
                        Text(text = "You have completed the following habits:",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth())
                        LazyVerticalGrid(columns = GridCells.Fixed(1)) {
                            items(state.habitList.size){
                                Text(text = "• " + state.habitList[it].habitName,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MonthlyHistory(onEvent: (HistoryEvent) -> Unit, state: HistoryState){
    /*
    Grid to display every day in of the month which its associated mood
     */
    LazyVerticalGrid(
        modifier = Modifier
            .padding(10.dp)
            .offset(y = (-16).dp),
        columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
) {
        val offsetDays = LocalDate.of(state.selectedYear, state.selectedMonth, 1).dayOfWeek.value - 1
        items(offsetDays){
            DisabledDay()
        }
        items(state.dayList.size){
            DailyBox(onEvent, state.dayList[it], state)
        }
    }
}

@Composable
fun MonthSelector(onEvent: (HistoryEvent) -> Unit, state: HistoryState){
    /*
    Picker where the calendar month can be selected
     */
    val months = state.monthsWithRecord.toList()
    ListItemPicker(
        modifier = Modifier
            .width(160.dp)
            .padding(horizontal = 10.dp),
        label = {Month.of(it%100).toString()},
        value = state.selectedYear * 100 + state.selectedMonth.value,
        onValueChange = { onEvent(HistoryEvent.ChangeCurrentMonth(it)) },
        list = months,
    )
}

@Composable
fun DisabledDay(){
    /*
    Button which represents a day of the previous month in the calendar
     */
    Button(onClick = { /*This button does nothing ;)*/ },
        modifier = Modifier.size(width = 25.dp, height = 25.dp),
        shape = RoundedCornerShape(5.dp),
        enabled = false) {
    }
}

@Composable
fun DailyBox(onEvent: (HistoryEvent) -> Unit, dayOfMonth: DayOfMonth, state: HistoryState){
    /*
    Button which acts as a day in the calendar, will show information about the day if clicked
     */
    Button(
        modifier = Modifier
            .size(width = 25.dp, height = 25.dp),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(dayOfMonth.colour),
        onClick = {
            onEvent(HistoryEvent.ChangeSelectedDay(dayOfMonth.date, dayOfMonth.mood))},
        border = if (dayOfMonth.date == state.selectedDate) {BorderStroke(1.dp, Color.Yellow)} else {null}

    ) {
    }
}

@Composable
fun Background(){
    val drawable = loadImageFromAssets(LocalContext.current, "images/boarderedbackground.png")
    val bitmap = drawable?.toBitmap()?.asImageBitmap()
    bitmap?.let { BitmapPainter(it) }?.let {
    Image(
        painter = it,
        contentDescription = null,
        alignment = Alignment.Center,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 400.dp) // to alter image height
        )
    }
}

@Composable
fun SunMary(onEvent: (HistoryEvent) -> Unit){
    val drawable = loadImageFromAssets(LocalContext.current, "images/sun.png")
    val bitmap = drawable!!.toBitmap()
    Image(
        painter = BitmapPainter(bitmap.asImageBitmap()),
        contentDescription = null,
        alignment = Alignment.Center,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(height = 80.dp)
            .offset(265.dp, 15.dp)
            .pointerInput(Unit) {
                detectTapGestures {
                    // calcs offset scale due to height setting
                    if (clickPixel(80.dp.toPx(), it, bitmap))
                        onEvent(HistoryEvent.ShowSummary)
                }
            }
    )
}

@Composable
fun FlowerClick(onEvent: (HistoryEvent) -> Unit, state: HistoryState, flower: String, offsetX: Dp, offsetY: Dp, habitRecord: HabitRecord) {
    val drawable = loadImageFromAssets(LocalContext.current, flower)
    val bitmap = drawable!!.toBitmap()
    Image(
        painter = BitmapPainter(bitmap.asImageBitmap()),
        contentDescription = null,
        alignment = Alignment.Center,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(height = 65.dp)
            .offset(offsetX, offsetY)
            .pointerInput(Unit) {
                detectTapGestures {
                    //if (clickPixel(65.dp.toPx(), it, bitmap)) {
                        //onEvent(HistoryEvent.NameTagToggle)
                        onEvent(
                            HistoryEvent.SetOffSet(
                                DpOffset(
                                    with(density) { (it.x).toDp() } + offsetX,
                                    with(density) { (it.y).toDp() } + offsetY
                                ),
                                habitRecord
                            )
                        )
                    }
                //}
            }
    )
}

@Composable
fun BushClick(onEvent: (HistoryEvent) -> Unit, state: HistoryState, bush: String, offsetX: Dp, offsetY: Dp, habitRecord: HabitRecord){
    val drawable = loadImageFromAssets(LocalContext.current, bush)
    val bitmap = drawable!!.toBitmap()
    Image(
        painter = BitmapPainter(bitmap.asImageBitmap()),
        contentDescription = null,
        alignment = Alignment.Center,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(height = 65.dp)
            .offset(offsetX, offsetY)
            .pointerInput(Unit) {
                detectTapGestures {
                    // calcs offset scale due to height setting

                    if (clickPixel(65.dp.toPx(), it, bitmap)) {
                        //onEvent(HistoryEvent.NameTagToggle)
                        onEvent(
                            HistoryEvent.SetOffSet(
                                DpOffset(
                                    with(density) { (it.x).toDp() } + offsetX,
                                    with(density) { (it.y).toDp() } + offsetY
                                ),
                                habitRecord
                            )
                        )
                        //onEvent(HistoryEvent.SelectPlant(habitRecord))
                    }
                }
            }
    )
}

@Composable
fun TreeClick(onEvent: (HistoryEvent) -> Unit, state: HistoryState, habitRecord: HabitRecord){
    val drawable = loadImageFromAssets(LocalContext.current, "images/tree.png")
    val bitmap = drawable!!.toBitmap()

    // all in box so offset is relative to sprite
    Image(
        painter = BitmapPainter(bitmap.asImageBitmap()),
        contentDescription = null,
        alignment = Alignment.Center,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(height = 225.dp)
            .offset(100.dp, 15.dp)
            .pointerInput(Unit) {
                detectTapGestures {
                    // calcs offset scale due to height setting
                    // check pixel is not transparent
                    if (clickPixel(225.dp.toPx(), it, bitmap))
                        //onEvent(HistoryEvent.NameTagToggle)
                        onEvent(
                            HistoryEvent.SetOffSet(
                            DpOffset(
                                with(density) { (it.x).toDp() } + 100.dp,
                                with(density) { (it.y).toDp() } + 15.dp
                            ),
                            habitRecord
                        )
                    )
                        //onEvent(HistoryEvent.SelectPlant(habitRecord))
                }
            }
    )
}



fun clickPixel(
    height: Float,
    it: Offset,
    map: Bitmap,
):Boolean{
    val scala = height / map.height
    // if real pixel show drop down menu and set off set based on click
    return map.getPixel(
        (it.x / scala).toInt(),
        (it.y / scala).toInt()
    ) != 0
}

fun loadImageFromAssets(context: Context, filename: String): Drawable? {
    try {
        val inputStream = context.assets.open(filename)
        return Drawable.createFromStream(inputStream, null)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

@Composable
fun NameTag(show: Boolean, showe: DpOffset, onEvent: (HistoryEvent) -> Unit, state: HistoryState ){
    DropdownMenu(
        expanded = show,
        onDismissRequest = {
            onEvent(HistoryEvent.NameTagToggle)
        },
        // add click off set to the sprite offset, y negative since it needs to go up
        offset = DpOffset(showe.x, (-400).dp + showe.y),
        modifier = Modifier.padding(horizontal = 10.dp).background(MaterialTheme.colorScheme.primary)
    ) {
        Text(text = state.habitStored!!.habitName)
    }
}