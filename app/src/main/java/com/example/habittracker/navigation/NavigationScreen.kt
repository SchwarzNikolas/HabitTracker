package com.example.habittracker.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.habittracker.CustomScreen
import com.example.habittracker.habit.HabitEvent
import com.example.habittracker.habit.HabitState
import com.example.habittracker.habit.MainScreen

@Composable
fun AppNavigation(
    state: HabitState,
    onEvent: (HabitEvent) -> Unit
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
                MainScreen(state = state, onEvent = onEvent)
            }
            composable(route = "Add"){
                CustomScreen()
            }
            composable(route = "History"){
                CustomScreen() // Change to history screen later
            }
        }
    }
}