package com.habittracker.rootreflect

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.habittracker.rootreflect.custom.CustomHabitEvent
import com.habittracker.rootreflect.custom.CustomViewModel
import com.habittracker.rootreflect.database.Habit
import com.habittracker.rootreflect.database.HabitDao
import com.habittracker.rootreflect.database.HabitDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class CustomHabitStateTest {
    private lateinit var dao: HabitDao
    private lateinit var db: HabitDatabase
    private lateinit var viewModel: CustomViewModel
    private lateinit var habitList: List<Habit>
    @Before
    fun setUp(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, HabitDatabase::class.java).build()
        dao = db.dao
        viewModel = CustomViewModel(dao)
    }

    @After
    @Throws(IOException::class)
    fun closeDB(){
        db.close()
    }

    @Test
    fun editNameTest(){
        val event = CustomHabitEvent.EditName("abc")
        viewModel.onEvent(event)
        assertThat("abc").isEqualTo(viewModel.state.value.habitName)
    }

    @Test
    fun editFreqTest(){
        val event = CustomHabitEvent.EditFreq(3)
        viewModel.onEvent(event)
        assertThat(viewModel.state.value.habitFrequency).isEqualTo(3)
    }

    @Test
    fun toggleDayTest(){
        val event = CustomHabitEvent.ToggleDay(3)
        viewModel.onEvent(event)
        assertThat(viewModel.state.value.completion[3].value).isEqualTo(true)
    }

    @Test
    fun updateDailyTest(){
        val event = CustomHabitEvent.UpdateDaily
        viewModel.onEvent(event)
        assertThat(viewModel.state.value.isDaily).isFalse()
    }

    @Test
    fun saveEditTest() = runBlocking{
        val event = CustomHabitEvent.SaveEdit

        viewModel.onEvent(event)
        dao.upsertHabit(Habit(name = "test123"))
        dao.fetchHabitByDay("1111111").test {
            habitList = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(habitList[0].name).isEqualTo("test123")
    }
}
