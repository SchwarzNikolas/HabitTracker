package com.example.habittracker

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.habittracker.custom.CustomHabitEvent
import com.example.habittracker.custom.CustomViewModel
import com.example.habittracker.database.Habit
import com.example.habittracker.database.HabitDao
import com.example.habittracker.database.HabitDatabase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import java.io.IOException
import org.junit.Test

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
        val event = CustomHabitEvent.EditFreq("3")
        viewModel.onEvent(event)
        assertThat("3").isEqualTo(viewModel.state.value.habitFrequency)
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
        dao.fetchHabits().test {
            habitList = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(habitList[0].name).isEqualTo("badminton")
    }
}
