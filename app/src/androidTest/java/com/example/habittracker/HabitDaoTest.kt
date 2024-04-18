package com.example.habittracker

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class HabitDaoTest {
    private lateinit var dao: HabitDao
    private lateinit var db: HabitDatabase
    private lateinit var viewModel: HabitViewModel
    private var habitList: List<Habit> = listOf()

    @Before
    fun createDB(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, HabitDatabase::class.java).build()
        dao = db.dao
        viewModel = HabitViewModel(dao)
        viewModel.viewModelScope.launch {
            upDateList()
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDB(){
        db.close()
    }

    @Test
    fun daoInsert(){
        val habit = Habit()
        assertTrue(habitList.isEmpty())
        viewModel.viewModelScope.launch {
            dao.insertHabit(habit)
            upDateList()
            assertTrue(habitList[0].name == habit.name)
        }
    }

    private suspend fun upDateList(){
            dao.fetchHabits().collect{habits -> run{habitList = habits}}
    }
}