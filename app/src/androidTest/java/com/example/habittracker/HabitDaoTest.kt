package com.example.habittracker

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.habittracker.database.Habit
import com.example.habittracker.database.HabitCompletion
import com.example.habittracker.database.HabitDao
import com.example.habittracker.database.HabitDatabase
import com.example.habittracker.database.HabitJoin
import com.example.habittracker.database.HabitRecord
import com.example.habittracker.database.MoodRecord
import com.example.habittracker.habit.HabitViewModel
import com.example.habittracker.mood.MoodType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
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
    private var habitCompletion: List<HabitJoin> = listOf()
    private lateinit var fetchMood: List<MoodRecord>

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
        // create habitRecord and insert it into the database
        val habitRecord = HabitRecord(0, "Testing", LocalDate.now().toString())
        dao.insertRecord(habitRecord)
        // fetch records and check if the inserted one is in the list
        dao.fetchHabitRecords().test {
            habitRecords = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(habitRecords[0].habitName).isEqualTo("Testing")
    }

    @Test
    fun deleteHabitRecordTest() = runBlocking {
        // create habitRecord and insert it into the database
        var habitRecord = HabitRecord(0, "Testing", LocalDate.now().toString())
        dao.insertRecord(habitRecord)
        habitRecord = HabitRecord(0, "Testing2", LocalDate.now().toString())
        dao.insertRecord(habitRecord)
        // fetch records and check if the inserted one is in the list
        dao.fetchHabitRecords().test {
            habitRecords = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        // check that record is in the database
        assertThat(habitRecords.size).isEqualTo(2)
        // delete record from the database, fetch all records and test if it got deleted
        dao.deleteRecord("Testing", LocalDate.now().toString())
        dao.fetchHabitRecords().test {
            habitRecords = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(habitRecords.size).isEqualTo(1)
    }

    @Test
    fun insertCompletionTest() = runBlocking {
        // create habitCompletion and insert it into the database
        val completedHabit = HabitCompletion(1, 0, false, "1111111")
        dao.insertHabit(Habit())
        dao.insertCompletion(completedHabit)
        // fetch completed habits from the database and compare it with created object
        dao.getHabit().test {
            habitCompletion = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(habitCompletion[0].completion).isEqualTo(completedHabit)
    }

    @Test
    fun deleteCompletionTest() = runBlocking {
        // create habitCompletion and insert them into the database
        val completedHabit = HabitCompletion(1, 0, false, "1111111")
        dao.insertHabit(Habit())
        dao.insertCompletion(completedHabit)
        // fetch completed habits from the database and check size
        dao.getHabit().test {
            habitCompletion = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(habitCompletion.size).isEqualTo(1)
        // delete completion from database
        dao.deleteCompletion(completedHabit)
        // fetch completed habits from the database and check size
        dao.getHabit().test {
            habitCompletion = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(habitCompletion.size).isEqualTo(0)
    }

    @Test
    fun updateCompletionTest() = runBlocking {
        // create habitCompletion and insert them into the database
        var completedHabit = HabitCompletion(1, 0, false, "1111111")
        dao.insertHabit(Habit())
        dao.insertCompletion(completedHabit)
        // update HabitCompletion and fetch the results
        completedHabit = HabitCompletion(1, 1, true, "1111111")
        dao.updateCompletion(completedHabit)
        dao.getHabit().test {
            habitCompletion = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        // check that data got updated
        assertThat(habitCompletion[0].completion.done).isEqualTo(true)
        assertThat(habitCompletion[0].completion.completion).isEqualTo(1)
    }

    @Test
    fun resetCompletionTest() = runBlocking {
        // create habitCompletion and insert them into the database
        val completedHabit = HabitCompletion(1, 1, true, "1111111")
        dao.insertHabit(Habit())
        dao.insertCompletion(completedHabit)
        // reset the completion and fetch all the habits
        dao.resetCompletion()
        dao.getHabit().test {
            habitCompletion = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        // check if the stats got reset
        assertThat(habitCompletion[0].completion.done).isEqualTo(false)
        assertThat(habitCompletion[0].completion.completion).isEqualTo(0)
    }

    @Test
    fun fetchHabitByDayTest() = runBlocking {
        // create habitCompletion and insert them into the database
        val completedHabit = HabitCompletion(1, 1, true, "1111111")
        dao.insertHabit(Habit())
        dao.insertCompletion(completedHabit)
        dao.fetchHabitByDay("1111111").test {
            habitCompletion = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(habitCompletion.size).isEqualTo(1)
    }

    @Test
    fun insertMoodRecTest() = runBlocking{
        val moodRecord = MoodRecord()
        dao.insertMoodRec(moodRecord)
        dao.fetchMoodRecords().test {
            fetchMood = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(fetchMood[0].mood).isEqualTo(moodRecord.mood)
    }

    @Test
    fun getMoodRecByDateTest() = runBlocking{
        val moodRecord = MoodRecord()
        dao.insertMoodRec(moodRecord)
        // replace String with LocalDate.now().toString() when date implemented
        val moodDate: MoodRecord? = dao.getMoodRecByDate("2025-05-05")
        assertThat(moodDate).isNotNull()
    }

    @Test
    fun updateMoodRecTest() = runBlocking {
        val moodRecord = MoodRecord()
        dao.insertMoodRec(moodRecord)
        dao.updateMoodRec("2025-05-05", MoodType.GOOD)
        dao.fetchMoodRecords().test {
            fetchMood = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(fetchMood[0].mood.moodColor).isEqualTo(0xFF008000)
    }

    @Test
    fun deleteMoodRecordTest() = runBlocking {
        var moodRecord = MoodRecord(1, "2024-05-05", MoodType.GOOD)
        dao.insertMoodRec(moodRecord)
        moodRecord = MoodRecord(2, "2025-05-05", MoodType.BAD)
        dao.insertMoodRec(moodRecord)
        dao.deleteMoodRecord("2025-05-05")
        dao.fetchMoodRecords().test {
            fetchMood = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(fetchMood.size).isEqualTo(1)
    }
}
