package com.example.habittracker

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.habittracker.database.HabitDao
import com.example.habittracker.database.HabitDatabase
import com.example.habittracker.database.Habit
import com.example.habittracker.habit.HabitViewModel
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
    fun closeDB() {
        db.close()
    }

    @Test
    fun daoInsert() {
        // create habit and check if habitList is empty
        val habit = Habit()
        assertTrue(habitList.isEmpty())
        // insert the habit into the database via the dao
        viewModel.viewModelScope.launch {
            dao.insertHabit(habit)
        }
        /*
            Wait for habit to be fetched from the database,
            will time out after 100ms
         */
        var cnt = 0
        while (habitList.size < 1 && cnt < 10){
            Thread.sleep(10)
            cnt++
        }
        // check if habit is in database
        assertTrue(habitList[0].name == habit.name)
    }

    private suspend fun upDateList(){
        dao.fetchHabits().collect{habits -> habitList = habits}
    }
}