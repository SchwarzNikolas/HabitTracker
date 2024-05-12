package com.habittracker.rootreflect.history

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import java.io.IOException
import java.time.Month


@Composable
fun HistoryScreen(
    state: HistoryState,
    onEvent: (HistoryEvent) -> Unit
) {
    Column {
        Text(text = "History Screen")
        Background(onEvent = onEvent, state = state)
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
            .align(Alignment.CenterHorizontally)){
            Column {
                Text(text = state.selectedDate.toString())
                Text(text = state.selectedMood)
            }
        }
    }
}

@Composable
fun MonthlyHistory(onEvent: (HistoryEvent) -> Unit, state: HistoryState){
    /*
    Grid to display every day in of the month which its associated mood
     */
    // TODO: place the first day of the month on the correct weekday
    LazyVerticalGrid(
        modifier = Modifier
            .padding(10.dp)
            .offset(y = (-16).dp),
        columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
) {
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
fun Background (onEvent: (HistoryEvent) -> Unit, state: HistoryState){
    val drawable = loadImageFromAssets(LocalContext.current, "images/background.png")
    val bitmap = drawable?.toBitmap()?.asImageBitmap()
    Box() {
        bitmap?.let { BitmapPainter(it) }?.let {
            Image(
                painter = it,
                contentDescription = null)
        }
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

