package com.example.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.habittracker.database.HabitDatabase
import com.example.habittracker.habit.HabitViewModel
import com.example.habittracker.mood.MoodViewModel
import com.example.habittracker.navigation.AppNavigation
import com.example.habittracker.ui.theme.HabitTrackerTheme

class MainActivity : ComponentActivity() {

    // Creating database
    private val db by lazy{
        Room.databaseBuilder(
            applicationContext,
            HabitDatabase::class.java,
            "habit.db"
        ).fallbackToDestructiveMigration().build()
    }

    // Creating view model for Habits
    private val habitViewModel by viewModels<HabitViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HabitViewModel(db.dao) as T
                }
            }
        }
    )

    // Creating view model for Habit-Creation
    private val customViewModel by viewModels<CustomViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CustomViewModel(db.dao) as T
                }
            }
        }
    )

    // Creating view model for Moods
    private val moodViewModel by viewModels<MoodViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MoodViewModel(db.dao) as T
                }
            }
        }
    )

    // when app launches runs continuously and handles UI theme and connects Viewmodel to the UI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HabitTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val habitState by habitViewModel.state.collectAsState()
//                    MainScreen(state = habitState, onEvent = habitViewModel::onEvent)
                    AppNavigation(
                        moodViewModel = moodViewModel,
                        customViewModel = customViewModel,
                        habitViewModel = habitViewModel
                    )
                }
            }
        }
    }
}
