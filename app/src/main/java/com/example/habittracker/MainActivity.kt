package com.example.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.habittracker.ui.theme.HabitTrackerTheme

class MainActivity : ComponentActivity() {

    // Creating database
    private val db by lazy{
        Room.databaseBuilder(
            applicationContext,
            HabitDatabase::class.java,
            "habit.db"
        ).build()
    }

    // Creating view model
    private val viewModel by viewModels<HabitViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HabitViewModel(db.dao) as T
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

                    val state by viewModel.state.collectAsState()
                    MainScreen(state = state, onEvent = viewModel::onEvent)
                }
            }
        }
    }
}
