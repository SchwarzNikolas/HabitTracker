package com.habittracker.rootreflect.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import com.habittracker.rootreflect.custom.CustomScreen
import com.habittracker.rootreflect.custom.CustomViewModel
import com.habittracker.rootreflect.habit.HabitViewModel
import com.habittracker.rootreflect.habit.MainScreen
import com.habittracker.rootreflect.history.HistoryScreen
import com.habittracker.rootreflect.history.HistoryViewModel


// Navigation Bar to switch to different screens
@Composable
fun AppNavigation(
    // get the viewmodels from the MainActivity
    customViewModel: CustomViewModel,
    habitViewModel: HabitViewModel,
    historyViewModel: HistoryViewModel
){
    val navController = rememberNavController() // The navcontroller is responsible to handle the page navigation
    val habitState by habitViewModel.state.collectAsState()
    val customState by customViewModel.state.collectAsState()
    val historyState by historyViewModel.state.collectAsState()

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
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedIconColor = MaterialTheme.colorScheme.tertiary,
                            unselectedTextColor = MaterialTheme.colorScheme.tertiary,
                            indicatorColor = MaterialTheme.colorScheme.background
                        )
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
                MainScreen(state = habitState, onEvent = habitViewModel::onEvent)
            }
            composable(route = "Add"){
                // link the CustomScreen to the second button
                CustomScreen(state = customState, onEvent = customViewModel::onEvent)
            }
            composable(route = "History"){
                // link the HistoryScreen to the third button
                HistoryScreen(state = historyState, onEvent = historyViewModel::onEvent)
            }
        }
    }
}