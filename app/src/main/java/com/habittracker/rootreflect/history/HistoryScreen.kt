package com.habittracker.rootreflect.history

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
import java.sql.Date
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

@Composable
fun HistoryScreen(
    state: HistoryState,
    onEvent: (HistoryEvent) -> Unit
) {
    Column {
        Text(text = "History Screen")
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
    // change parsing after date is used in mood
    val buttonDate = "20"+LocalDate.now().year.toString()+"-"+(state.selectedMonth.ordinal + 1).toString()+"-"+(dayNum+1).toString()
    Text(text = buttonDate)
    Button(
        modifier = Modifier
            .size(width = 25.dp, height = 25.dp),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(Color(state.selectedMood.moodColor)),
        onClick = {
            onEvent(HistoryEvent.EnableBottomSheet)
            onEvent(HistoryEvent.ChangeSelectedMood(MoodType.BAD))}
    ) {
    }
}