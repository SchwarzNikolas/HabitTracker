package com.habittracker.rootreflect

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.habittracker.rootreflect.database.Habit
import com.habittracker.rootreflect.database.HabitDao
import com.habittracker.rootreflect.database.HabitDatabase
import com.habittracker.rootreflect.database.HabitRecord
import com.habittracker.rootreflect.database.MoodRecord
import com.habittracker.rootreflect.habit.HabitViewModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.time.LocalDate

class HabitDaoTest {
    /*
    Test all the methods of the dao.
    In this suite the turbine library will be used to fetch information about a flow.
     */
    private lateinit var dao: HabitDao
    private lateinit var db: HabitDatabase
    private lateinit var viewModel: HabitViewModel
    private var habitList: List<Habit> = listOf()
    private var habitRecords: List<HabitRecord> = listOf()
    private var habits: List<Habit> = listOf()
    private var fetchDates: List<LocalDate> = listOf()

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
        // create habit and check if habitList is empty
        val habit = Habit()
        // insert the habit into the database via the dao
        dao.upsertHabit(habit)
        // the .test method waits for the flow to return an item and then cancels method call
        dao.fetchHabitByDay("1111111").test {
            habitList = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        // check that the returned habit name is the same as the inserted one
        assertThat(habitList[0].name).isEqualTo(habit.name)
    }

    @Test
    fun daoDeleteTest() = runBlocking {
        val habit1 = Habit(name = "test1")
        val habit2 = Habit(name = "test2")
        // insert the 2 habits into the database via the dao
        dao.upsertHabit(habit1)
        dao.upsertHabit(habit2)
        // fetch the habits from the database and then delete the first one in the list
        dao.fetchHabitByDay("1111111").test {
            habitList = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        dao.deleteHabit(habitList[0])
        // fetch the habitList again
        dao.fetchHabitByDay("1111111").test {
            habitList = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        // now check if only one habit is in the list
        assertThat(habitList.size).isEqualTo(1)
    }

    @Test
    fun updateHabitTest() = runBlocking {
        // create habit and insert it into the database
        var habit = Habit()
        dao.upsertHabit(habit)
        // fetch habit from the database and check if it exists
        dao.fetchHabitByDay("1111111").test {
            habitList = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(habitList.size).isEqualTo(1)
        // update the habits frequency, name and id
        habit = Habit(habitList[0].habitId, "Testing", 2)
        dao.upsertHabit(habit)
        // fetch habit from the database and check if it updated
        dao.fetchHabitByDay("1111111").test {
            habitList = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(habitList.size).isEqualTo(1)
        assertThat(habitList[0].name).isEqualTo("Testing")
        assertThat(habitList[0].frequency).isEqualTo(2)
    }

    @Test
    fun insertHabitRecordTest() = runBlocking {
        // create habitRecord and insert it into the database
        val habitRecord = HabitRecord( "Testing", 1, LocalDate.now())
        dao.upsertRecord(habitRecord)
        // fetch records and check if the inserted one is in the list
        dao.fetchHabitRecordsByDate(LocalDate.now()).test {
            habitRecords = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(habitRecords[0].habitName).isEqualTo("Testing")
    }

    @Test
    fun deleteHabitRecordTest() = runBlocking {
        // create habitRecord and insert it into the database
        var habitRecord = HabitRecord("Testing", 1, LocalDate.now())
        dao.upsertRecord(habitRecord)
        habitRecord = HabitRecord( "Testing2", 1, LocalDate.now())
        dao.upsertRecord(habitRecord)
        // fetch records and check if the inserted one is in the list
        dao.fetchHabitRecordsByDate(LocalDate.now()).test {
            habitRecords = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        // check that record is in the database
        assertThat(habitRecords.size).isEqualTo(2)
        // delete record from the database, fetch all records and test if it got deleted
        dao.deleteRecord("Testing", LocalDate.now())
        dao.fetchHabitRecordsByDate(LocalDate.now()).test {
            habitRecords = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(habitRecords.size).isEqualTo(1)
    }

    @Test
    fun resetCompletionTest() = runBlocking {
        // create habitCompletion and insert them into the database
        //val completedHabit = HabitCompletion(1, 1, true, "1111111")
        dao.upsertHabit(Habit(done = true, completion = 2))
        //dao.upsertCompletion(completedHabit)
        // reset the completion and fetch all the habits
        dao.resetCompletion()
        dao.fetchHabitByDay("1111111").test {
            habits = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        // check if the stats got reset
        assertThat(habits[0].done).isEqualTo(false)
        assertThat(habits[0].completion).isEqualTo(0)
    }

    @Test
    fun fetchHabitByDayTest() = runBlocking {
        // create habitCompletion and insert them into the database
        //val completedHabit = HabitCompletion(1, 1, true, "1111111")
        dao.upsertHabit(Habit())
        //dao.upsertCompletion(completedHabit)
        dao.fetchHabitByDay("1111111").test {
            habits = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(habits.size).isEqualTo(1)
    }

    @Test
    fun upsertMoodRecTest() = runBlocking{
        val moodRecord = MoodRecord(LocalDate.now())
        val fetchMood: MoodRecord?
        dao.upsertMoodRec(moodRecord)
        fetchMood = dao.getMoodRecByDate(LocalDate.now())
        Assert.assertNotNull(fetchMood)
    }

    @Test
    fun getMoodRecByDateTest() = runBlocking{
        val moodRecord = MoodRecord(LocalDate.now())
        dao.upsertMoodRec(moodRecord)
        // replace String with LocalDate.now().toString() when date implemented
        val moodDate: MoodRecord? = dao.getMoodRecByDate(LocalDate.now())
        assertThat(moodDate).isNotNull()
    }
    @Test
    fun fetchDatesTest() = runBlocking {
        dao.upsertMoodRec(moodRec = MoodRecord(LocalDate.now()))
        dao.fetchDates().test {
            fetchDates = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(fetchDates[0]).isEqualTo(LocalDate.now())
    }
}