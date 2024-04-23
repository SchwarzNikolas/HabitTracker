package com.example.habittracker

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.habittracker.database.HabitDao
import com.example.habittracker.database.HabitDatabase
import com.example.habittracker.habit.DisplayHabit
import com.example.habittracker.habit.HabitEvent
import com.example.habittracker.habit.HabitViewModel
import kotlinx.coroutines.flow.update
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
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
    fun onEventtest() {
        assertEquals("test", viewModel.state.value.name)
    }

    @Test
    fun updateEditString(){
        val event = HabitEvent.UpDateEditString("test123")
        viewModel.onEvent(event)
        assertEquals("test123", viewModel.state.value.editString)
    }

    @Test
    fun updateEditFreq(){
        val event = HabitEvent.UpDateEditFreq("10")
        viewModel.onEvent(event)
        assertEquals("10", viewModel.state.value.editFreq)
    }
    @Test
    fun cancelEdit(){
        val event = HabitEvent.CancelEdit
        viewModel.state.update { it.copy(showEdit = true) }
        assertTrue(viewModel.state.value.showEdit)
        viewModel.onEvent(event)
        assertFalse(viewModel.state.value.showEdit)
    }

    @Test
    fun deleteHabit(){
        val displayHabit = DisplayHabit()


        // event = HabitEvent.DeleteHabit(displayHabit)

    }

}