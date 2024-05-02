package com.example.habittracker.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.habittracker.custom.CustomScreen
import com.example.habittracker.custom.CustomViewModel
import com.example.habittracker.habit.HabitViewModel
import com.example.habittracker.habit.MainScreen
import com.example.habittracker.mood.MoodState
import com.example.habittracker.mood.MoodViewModel

// Navigation Bar to switch to different screens
@Composable
fun AppNavigation(
    // get the viewmodels from the MainActivity
    moodViewModel: MoodViewModel,
    customViewModel: CustomViewModel,
    habitViewModel: HabitViewModel
){
    val navController = rememberNavController() // The navcontroller is responsible to handle the page navigation
    val habitState by habitViewModel.state.collectAsState()
    val moodState by moodViewModel.state.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar{
                // get the state of the navcontroller
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                // go trough each navigation Destination and set their Icon and Action
                destinationList.forEach { navigationDestination ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any {it.route == navigationDestination.label} == true,
                        onClick = {
                            navController.navigate(navigationDestination.label){
                                popUpTo(navigationDestination.label){
                                    inclusive = true
                                }
                            }
                        },
                        icon = {
                               Icon(
                                   imageVector = navigationDestination.icon,
                                   contentDescription = null
                               )
                       },
                    )
                }
            }
        }
    )
    {paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "Home", // set the default page, when the app is started
            modifier = Modifier
                .padding(paddingValues)
        ){
            composable(route = "Home"){
                // link the MainScreen to the first button
                MainScreen(state = habitState, moodState = moodState, onEvent = habitViewModel::onEvent, onMoodEvent = moodViewModel::onEvent)
            }
            composable(route = "Add"){
                // link the CustomScreen to the second button
                val customState by customViewModel.state.collectAsState()
                CustomScreen(state = customState, onEvent = customViewModel::onEvent)
            }
            composable(route = "History"){
                // link the HistoryScreen to the third button
                val customState by customViewModel.state.collectAsState() // Change to history state
                CustomScreen(state = customState, onEvent = customViewModel::onEvent) // Change to history screen later
            }
        }
    }
}