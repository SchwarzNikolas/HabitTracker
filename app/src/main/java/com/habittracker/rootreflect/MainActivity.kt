package com.habittracker.rootreflect

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
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
import com.habittracker.rootreflect.custom.CustomViewModel
import com.habittracker.rootreflect.database.HabitDatabase
import com.habittracker.rootreflect.habit.HabitViewModel
import com.habittracker.rootreflect.history.HistoryViewModel
import com.habittracker.rootreflect.navigation.AppNavigation
import com.habittracker.rootreflect.notification.NotificationService
import com.habittracker.rootreflect.ui.theme.HabitTrackerTheme

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

    // Creating view model for History
    private val historyViewModel by viewModels<HistoryViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HistoryViewModel(db.dao) as T
                }
            }
        }
    )

    // when app launches runs continuously and handles UI theme and connects Viewmodel to the UI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContent {
            HabitTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val habitState by habitViewModel.state.collectAsState()
//                    MainScreen(state = habitState, onEvent = habitViewModel::onEvent)
                    AppNavigation(
                        customViewModel = customViewModel,
                        habitViewModel = habitViewModel,
                        historyViewModel = historyViewModel
                    )
                }
            }
        }

        val notification = NotificationService(applicationContext)
        notification.scheduleDailyNotification(19, 21)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NotificationService.COUNTER_CHANNEL_ID,
            "Notification",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Used for mood reminder"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


}
