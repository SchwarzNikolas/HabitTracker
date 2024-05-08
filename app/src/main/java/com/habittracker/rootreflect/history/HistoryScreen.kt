package com.habittracker.rootreflect.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.habittracker.rootreflect.custom.CustomHabitEvent
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
        Row {
            MonthSelector(onEvent = onEvent, state = state)
            MonthlyHistory(state = state)
        }
    }
}

@Composable
fun MonthlyHistory(state: HistoryState){
    val year: YearMonth = YearMonth.of(2024, state.selectedMonth)
    val amountDays: Int = year.lengthOfMonth()
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
) {
        items(amountDays){
            DailyBox()
        }
    }
}

@Composable
fun MonthSelector(onEvent: (HistoryEvent) -> Unit, state: HistoryState){
    val months = Month.entries
    ListItemPicker(
        modifier = Modifier.width(150.dp),
        label = {it.toString()},
        value = state.selectedMonth,
        onValueChange = { onEvent(HistoryEvent.ChangeCurrentMonth(it)) },
        list = months,
    )
}

@Composable
fun DailyBox(
){
    Button(
        modifier = Modifier
            .size(width = 25.dp, height = 25.dp),
        shape = RoundedCornerShape(2.dp),
        onClick = { /**/ }
    ) {

    }
}