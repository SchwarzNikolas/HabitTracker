package com.example.habittracker

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.habittracker.database.Habit
import com.example.habittracker.database.HabitDao
import com.example.habittracker.database.HabitDatabase
import com.example.habittracker.database.HabitRecord
import com.example.habittracker.habit.HabitViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.time.LocalDate

class HabitDaoTest {
    private lateinit var dao: HabitDao
    private lateinit var db: HabitDatabase
    private lateinit var viewModel: HabitViewModel
    private var habitList: List<Habit> = listOf()

    @Before
    fun setUp(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, HabitDatabase::class.java).build()
        dao = db.dao
        viewModel = HabitViewModel(dao)
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }


    @Test
    fun daoInsertTest() = runBlocking{
        // var habitList: List<Habit> = listOf()
        // create habit and check if habitList is empty
        val habit = Habit()
        // insert the habit into the database via the dao
        dao.insertHabit(habit)
        // the .test method waits for the flow to return an item and then cancels method call
        dao.fetchHabits().test {
            habitList = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        // check that the returned habit name is the same as the inserted one
        assertThat(habitList[0].name).isEqualTo(habit.name)
    }

    @Test
    fun daoDeleteTest() = runBlocking {
        val habit = Habit()
        // insert the 2 habits into the database via the dao
        dao.insertHabit(habit)
        dao.insertHabit(habit)
        // fetch the habits from the database and then delete the first one in the list
        dao.fetchHabits().test {
            habitList = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        dao.deleteHabit(habitList[0])
        // fetch the habitList again
        dao.fetchHabits().test {
            habitList = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        // now check if only one habit is in the list
        assertThat(habitList.size).isEqualTo(1)
    }

    @Test
    fun updateHabitTest() = runBlocking {
        // var habitList: List<Habit> = listOf()
        // create habit and insert it into the database
        var habit = Habit()
        dao.insertHabit(habit)
        // fetch habit from the database and check if it exists
        dao.fetchHabits().test {
            habitList = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(habitList.size).isEqualTo(1)
        // update the habits frequency, name and id
        habit = Habit(habitList[0].habitId, "Testing", 2)
        dao.updateHabit(habit)
        // fetch habit from the database and check if it updated
        dao.fetchHabits().test {
            habitList = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(habitList.size).isEqualTo(1)
        assertThat(habitList[0].name).isEqualTo("Testing")
        assertThat(habitList[0].frequency).isEqualTo(2)
    }

    @Test
    fun insertHabitRecordTest() = runBlocking {
        var habitRecords: List<HabitRecord> = listOf()
        // create habitRecord and insert it into the database
        val habitRecord = HabitRecord(0, "Testing", LocalDate.now().toString())
        dao.insertRecord(habitRecord)
        // fetch records and check if the inserted one is in the list
        dao.fetchHabitRecords().test {
            habitRecords = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(habitRecords.size).isEqualTo(1)
        assertThat(habitRecords[0].habitName).isEqualTo("Testing")
    }
}