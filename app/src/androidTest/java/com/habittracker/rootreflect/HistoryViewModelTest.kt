package com.habittracker.rootreflect

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.habittracker.rootreflect.database.HabitDao
import com.habittracker.rootreflect.database.HabitDatabase
import com.habittracker.rootreflect.history.HistoryEvent
import com.habittracker.rootreflect.history.HistoryState
import com.habittracker.rootreflect.history.HistoryViewModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class HistoryViewModelTest {
    private lateinit var dao: HabitDao
    private lateinit var db: HabitDatabase
    private lateinit var viewModel: HistoryViewModel
    private lateinit var state: HistoryState

    @Before
    fun createDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, HabitDatabase::class.java).build()
        dao = db.dao
        viewModel = HistoryViewModel(dao)
        state = HistoryState()
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }

    @Test
    fun enableBottomSheetTest() = runBlocking {
        val event = HistoryEvent.EnableBottomSheet
        viewModel.onEvent(event)
        assertThat(viewModel.state.value.bottomSheetActive).isEqualTo(true)
    }

    /*@Test
    fun updateEditString(){
        val event = HabitEvent.UpDateEditString("test123")
        viewModel.onEvent(event)
        Assert.assertEquals("test123", viewModel.state.value.editString)
    }*/

}