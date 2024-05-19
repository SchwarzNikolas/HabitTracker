package com.habittracker.rootreflect

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.habittracker.rootreflect.database.HabitDao
import com.habittracker.rootreflect.database.HabitDatabase
import com.habittracker.rootreflect.database.HabitRecord
import com.habittracker.rootreflect.habit.MoodType
import com.habittracker.rootreflect.history.HistoryEvent
import com.habittracker.rootreflect.history.HistoryState
import com.habittracker.rootreflect.history.HistoryViewModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class HistoryViewModelTest {
    private lateinit var dao: HabitDao
    private lateinit var db: HabitDatabase
    private lateinit var viewModel: HistoryViewModel
    private lateinit var state: HistoryState
    private val date = LocalDate.now()
    private var habitRecords: List<HabitRecord> = listOf()

    @Before
    fun createDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, HabitDatabase::class.java).build()
        dao = db.dao
        viewModel = HistoryViewModel(dao)
        state = HistoryState()
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }

    @Test
    fun selectPlantTest() = runBlocking {
        val habitRec = HabitRecord("test",4,date)
        val event = HistoryEvent.SelectPlant(habitRec)
        viewModel.onEvent(event)
        assertThat(viewModel.state.value.bottomSheetActive).isTrue()
        assertThat(viewModel.state.value.habitInfo).isTrue()
        assertThat(viewModel.state.value.habitStored).isEqualTo(habitRec)
    }

    @Test
    fun enableBottomSheetTest() = runBlocking {
        val event = HistoryEvent.EnableBottomSheet
        viewModel.onEvent(event)
        assertThat(viewModel.state.value.bottomSheetActive).isEqualTo(true)
    }
    @Test
    fun disableBottomSheetTest() = runBlocking {
        val event = HistoryEvent.DisableBottomSheet
        viewModel.onEvent(event)
        assertThat(viewModel.state.value.bottomSheetActive).isEqualTo(false)
    }

    @Test
    fun changeCurrentMonthTest() = runBlocking {
        val event = HistoryEvent.ChangeCurrentMonth(202505)
        viewModel.onEvent(event)
        assertThat(viewModel.state.value.selectedMonth.value).isEqualTo(5)
        assertThat(viewModel.state.value.selectedYear).isEqualTo(2025)
    }

    @Test
    fun changeSelectedDayTest() = runBlocking {
        val event = HistoryEvent.ChangeSelectedDay(date, MoodType.OK.toString())
        val habitRec1 = HabitRecord("test2",2, date)
        val habitRec2 = HabitRecord("test3",3, date)
        dao.upsertRecord(habitRec1)
        dao.upsertRecord(habitRec2)
        viewModel.onEvent(event)
        dao.fetchHabitRecordsByDate(date).test {
            habitRecords = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        //assertThat(viewModel.state.value.habitList[0]).isEqualTo(habitRec1)
        assertThat(viewModel.state.value.selectedDate).isEqualTo(date)
        assertThat(viewModel.state.value.selectedMood).isEqualTo("OK")
        assertThat(viewModel.state.value.habitInfo).isFalse()
        assertThat(viewModel.state.value.habitListF1).isEmpty()
        assertThat(viewModel.state.value.habitListF2[0]).isEqualTo(habitRec1)
        assertThat(viewModel.state.value.habitListF3Above[0]).isEqualTo(habitRec2)
    }
}