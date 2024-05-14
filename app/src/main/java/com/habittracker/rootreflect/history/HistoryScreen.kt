package com.habittracker.rootreflect.history

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import java.io.IOException
import java.time.LocalDate
import java.time.Month


@Composable
fun HistoryScreen(
    state: HistoryState,
    onEvent: (HistoryEvent) -> Unit
) {
    Column {
        Text(text = "History Screen")
        Box {
            Background()
            TreeClick(onEvent = onEvent, state = state)
            BushClick(onEvent = onEvent, state = state, bush = "images/bush1.png", offsetX = 50.dp, offsetY = 200.dp ) // do again with bush random return val
            BushClick(onEvent = onEvent, state = state, bush = "images/bush2.png", offsetX = 160.dp, offsetY = 250.dp)
            BushClick(onEvent = onEvent, state = state, bush = "images/bush3.png", offsetX = 260.dp, offsetY = 220.dp)
            FlowerClick(onEvent = onEvent, state = state, flower = "images/blueflower.png", offsetX = 235.dp, offsetY = 295.dp)
            FlowerClick(onEvent = onEvent, state = state, flower = "images/orangeflower.png", offsetX = 80.dp, offsetY = 275.dp)
            FlowerClick(onEvent = onEvent, state = state, flower = "images/pinkflower.png", offsetX = 115.dp, offsetY = 300.dp)
            FlowerClick(onEvent = onEvent, state = state, flower = "images/yelloworangeflower.png", offsetX = 300.dp, offsetY = 310.dp)
            FlowerClick(onEvent = onEvent, state = state, flower = "images/blueflower.png", offsetX = 60.dp, offsetY = 320.dp)
            FlowerClick(onEvent = onEvent, state = state, flower = "images/yelloworangeflower.png", offsetX = 170.dp, offsetY = 315.dp)
            NameTag(show = state.nameTagActive, showe = state.offset, onEvent)

        }
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
            .height(500.dp)
            .align(Alignment.CenterHorizontally)
            .padding(10.dp)){
            if (state.habitInfo) {
                // display information about habits
            }
            else {
                Column {
                    if (state.selectedMood != "No mood") {
                        Text(
                            text = "On the " + state.selectedDate?.dayOfMonth.toString() + " of "
                                    + state.selectedDate?.month?.name?.substring(0, 1)
                                    + state.selectedDate?.month?.name?.substring(1)?.lowercase()
                                    + " " + state.selectedYear.toString()
                                    + " you felt " + state.selectedMood.lowercase() + ".",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    else {
                        Text(
                            text = "On the " + state.selectedDate?.dayOfMonth.toString() + " of "
                                + state.selectedDate?.month?.name?.substring(0, 1)
                                + state.selectedDate?.month?.name?.substring(1)?.lowercase()
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
                            textAlign = TextAlign.Center)
                        for (habit in state.habitList){
                            Text(text = "• " + habit.habitName,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth())
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
            DailyBox(onEvent, state.dayList[it])
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
fun DailyBox(onEvent: (HistoryEvent) -> Unit, dayOfMonth: DayOfMonth){
    /*
    Button which acts as a day in the calendar, will show information about the day if clicked
     */
    Button(
        modifier = Modifier
            .size(width = 25.dp, height = 25.dp),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(Color(dayOfMonth.colour)),
        onClick = {
            onEvent(HistoryEvent.EnableBottomSheet)
            onEvent(HistoryEvent.ChangeSelectedDay(dayOfMonth.date, dayOfMonth.mood))}
    ) {
    }
}

@Composable
fun Background(){
    val drawable = loadImageFromAssets(LocalContext.current, "images/backgroundboardered.png")
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
fun FlowerClick(onEvent: (HistoryEvent) -> Unit, state: HistoryState, flower: String, offsetX: Dp, offsetY: Dp){
    val drawable = loadImageFromAssets(LocalContext.current, flower)
    val bitmap = drawable!!.toBitmap()
    val den = LocalDensity.current
    Image(
        painter = BitmapPainter(bitmap.asImageBitmap()),
        contentDescription = null,
        alignment = Alignment.Center,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            //.fillMaxWidth()
            .height(height = 65.dp)
            .offset(offsetX, offsetY)
            .pointerInput(Unit) {
                detectTapGestures {
                    clickPixel(65.dp.toPx(), it, bitmap, onEvent, den, offsetX, offsetY)

                }
            }
    )
}

//@Composable
//fun FlowerRandom(){
//
//}

@Composable
fun BushClick(onEvent: (HistoryEvent) -> Unit, state: HistoryState, bush: String, offsetX: Dp, offsetY: Dp){
    val drawable = loadImageFromAssets(LocalContext.current, bush)
    val bitmap = drawable!!.toBitmap()
    val den = LocalDensity.current
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
                    clickPixel(65.dp.toPx(), it, bitmap, onEvent, den, offsetX, offsetY)
                }
            }
    )
}

//@Composable
//fun BushRandom(){
//
//}

@Composable
fun TreeClick(onEvent: (HistoryEvent) -> Unit, state: HistoryState){
    val drawable = loadImageFromAssets(LocalContext.current, "images/tree.png")
    val bitmap = drawable!!.toBitmap()
    // all in box so offset is relative to sprite
    val den = LocalDensity.current
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
                    clickPixel(225.dp.toPx(), it, bitmap, onEvent, den, 100.dp, 15.dp)
                }
            }
    )
}

@Composable
fun NameTag(show: Boolean, showe: DpOffset, onEvent: (HistoryEvent) -> Unit ){
    DropdownMenu(
        expanded = show,
        onDismissRequest = {
            onEvent(HistoryEvent.NameTagToggle)
        },
        // add click off set to the sprite offset, y negative since it needs to go up
        offset = DpOffset(showe.x, (-400).dp + showe.y),
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        Text(text = "habit name")
    }
}


fun clickPixel(
    height: Float,
    it: Offset,
    map: Bitmap,
    onEvent: (HistoryEvent) -> Unit,
    density: Density,
    x: Dp,
    y: Dp
){
    val scala = height / map.height
    if (map.getPixel(
            (it.x / scala).toInt(),
            (it.y / scala).toInt()
        ) != 0
    ) {
        // if real pixel show drop down menu and set off set based on click
        onEvent(HistoryEvent.NameTagToggle)
        onEvent(HistoryEvent.SetOffSet(DpOffset(with(density){ (it.x).toDp()}+x, with(density){ (it.y).toDp()}+y)))
    }
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