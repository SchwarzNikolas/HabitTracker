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
import com.example.habittracker.CustomHabitEvent
import com.example.habittracker.CustomScreen
import com.example.habittracker.CustomState
import com.example.habittracker.CustomViewModel
import com.example.habittracker.HabitEvent
import com.example.habittracker.HabitState
import com.example.habittracker.HabitViewModel
import com.example.habittracker.MainScreen

@Composable
fun AppNavigation(
    customViewModel: CustomViewModel,
    habitViewModel: HabitViewModel
){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar{
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                destinationList.forEach { navigationDestination ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any {it.route == navigationDestination.label} == true,
                        onClick = {
                            navController.navigate(navigationDestination.label)
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
            startDestination = "Home",
            modifier = Modifier
                .padding(paddingValues)
        ){
            composable(route = "Home"){
                val habitState by habitViewModel.state.collectAsState()
                MainScreen(state = habitState, onEvent = habitViewModel::onEvent)
            }
            composable(route = "Add"){
                val customState by customViewModel.state.collectAsState()
                CustomScreen(state = customState, onEvent = customViewModel::onEvent)
            }
            composable(route = "History"){
                val customState by customViewModel.state.collectAsState() // Change to history state
                CustomScreen(state = customState, onEvent = customViewModel::onEvent) // Change to history screen later
            }
        }
    }
}