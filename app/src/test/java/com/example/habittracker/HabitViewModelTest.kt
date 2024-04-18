package com.example.habittracker

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.IOException

class HabitViewModelTest {

    private  lateinit var dao: HabitDao
    private lateinit var db:HabitDatabase
    private var viewModel = HabitViewModel(dao)
    @Before
    fun createDB(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, HabitDatabase::class.java).build()
        dao = db.dao
    }

    @Test
    fun onEvent() {
        assertEquals("test", viewModel.state.value.name)
    }

    @After
    @Throws(IOException::class)
    fun closeDB(){
        db.close()
    }


}