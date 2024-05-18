package com.habittracker.rootreflect

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.habittracker.rootreflect.database.HabitDao
import com.habittracker.rootreflect.database.HabitDatabase
import com.habittracker.rootreflect.database.MoodRecord
import com.habittracker.rootreflect.habit.DisplayHabit
import com.habittracker.rootreflect.habit.HabitEvent
import com.habittracker.rootreflect.habit.HabitViewModel
import com.habittracker.rootreflect.habit.MoodType
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class HabitViewModelTest {
    private lateinit var dao: HabitDao
    private lateinit var db: HabitDatabase
    private lateinit var viewModel: HabitViewModel
    private var moodRecs: List<MoodRecord> = listOf()
    @Before
    fun createDB(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, HabitDatabase::class.java).build()
        dao = db.dao
        viewModel = HabitViewModel(dao)
    }

    @After
    @Throws(IOException::class)
    fun closeDB(){
        db.close()
    }

    @Test
    fun updateEditString(){
        val event = HabitEvent.UpDateEditString("test123")
        viewModel.onEvent(event)
        assertEquals("test123", viewModel.state.value.editString)
    }

    @Test
    fun updateEditFreqOutOfRange(){
        val event = HabitEvent.UpDateEditFreq(10)
        viewModel.onEvent(event)
        assertNotEquals(10, viewModel.state.value.editFreq)
    }
    @Test
    fun updateEditFreq(){
        val event = HabitEvent.UpDateEditFreq(5)
        viewModel.onEvent(event)
        assertEquals(5, viewModel.state.value.editFreq)
    }
//    @Test
//    fun cancelEdit(){
//        val event = HabitEvent.CancelEdit
//        viewModel.state.update { it.copy(showEdit = true) }
//        assertTrue(viewModel.state.value.showEdit)
//        viewModel.onEvent(event)
//        assertFalse(viewModel.state.value.showEdit)
//    }

    @Test
    fun deleteHabit(){
        val displayHabit = DisplayHabit()


        // event = HabitEvent.DeleteHabit(displayHabit)

    }

    // Uncomment the lines 206 & 210 in HabitViewModel when running the test
    @Test
    fun moodSelectedTest() = runBlocking {
        delay(500)
        val event = HabitEvent.MoodSelected(MoodType.GOOD)
        viewModel.onEvent(event)
        Truth.assertThat(viewModel.state.value.selectedMood).isEqualTo(MoodType.GOOD)

        dao.fetchMoodRecords().test {
            moodRecs = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        Truth.assertThat(moodRecs.size).isEqualTo(1)
    }
}