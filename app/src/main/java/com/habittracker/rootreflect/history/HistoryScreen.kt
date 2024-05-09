package com.habittracker.rootreflect.history

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
import androidx.compose.ui.unit.dp
import com.habittracker.rootreflect.mood.MoodType
import java.io.IOException
import java.time.LocalDate
import java.time.Month
import android.content.Context
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap


@Composable
fun HistoryScreen(
    state: HistoryState,
    onEvent: (HistoryEvent) -> Unit
) {
    Column {
        Text(text = "History Screen")
        Background(onEvent = onEvent, state = state)
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.height(165.dp)) {
            MonthSelector(onEvent = onEvent, state = state)
            MonthlyHistory(onEvent = onEvent, state = state)
        }
        if (state.bottomSheetActive){
            InfoSheet(onEvent = onEvent, state = state)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoSheet(onEvent: (HistoryEvent) -> Unit, state: HistoryState){
    ModalBottomSheet(
        onDismissRequest = {
            onEvent(HistoryEvent.DisableBottomSheet)
        },
    ) {
        Box(modifier = Modifier
            .height(500.dp)
            .align(Alignment.CenterHorizontally)){
            Button(
                onClick = { onEvent(HistoryEvent.DisableBottomSheet)}) {
                Text("Hide bottom sheet")
            }
        }
    }
}

@Composable
fun MonthlyHistory(onEvent: (HistoryEvent) -> Unit, state: HistoryState){
    LazyVerticalGrid(
        modifier = Modifier
            .padding(10.dp)
            .offset(y = (-16).dp),
        columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
) {
        items(state.dayList.size){
            DailyBox(onEvent, state, it)
        }
    }
}
@Composable
fun MonthSelector(onEvent: (HistoryEvent) -> Unit, state: HistoryState){
    val months = Month.entries
    ListItemPicker(
        modifier = Modifier
            .width(160.dp)
            .padding(horizontal = 10.dp),
        label = {it.toString()},
        value = state.selectedMonth,
        onValueChange = { onEvent(HistoryEvent.ChangeCurrentMonth(it)) },
        list = months,
    )
}

@Composable
fun DailyBox(onEvent: (HistoryEvent) -> Unit, state: HistoryState, dayNum: Int){
    Button(
        modifier = Modifier
            .size(width = 25.dp, height = 25.dp),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(Color(state.dayList[dayNum].colour)),
        onClick = {
            onEvent(HistoryEvent.EnableBottomSheet)}
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

