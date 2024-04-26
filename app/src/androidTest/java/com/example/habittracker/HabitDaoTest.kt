package com.example.habittracker

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.habittracker.database.Habit
import com.example.habittracker.database.HabitDao
import com.example.habittracker.database.HabitDatabase
import com.example.habittracker.habit.HabitViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class HabitDaoTest {
    private lateinit var dao: HabitDao
    private lateinit var db: HabitDatabase
    private lateinit var viewModel: HabitViewModel

    @Before
    fun setUp(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, HabitDatabase::class.java).build()
        dao = db.dao
        viewModel = HabitViewModel(dao)
    }


    @Test
    fun daoInsertTest() = runBlocking{
        var habitList: List<Habit> = listOf()
        // create habit and check if habitList is empty
        val habit = Habit()
        // insert the habit into the database via the dao
        dao.insertHabit(habit)
        // the .test method waits for the flow to return an item and then cancels method call
        dao.fetchHabits().test {
            habitList = awaitItem()
            cancel()
        }
        // check that the returned habit name is the same as the inserted one
        assertThat(habitList[0].name).isEqualTo(habit.name)
    }

}