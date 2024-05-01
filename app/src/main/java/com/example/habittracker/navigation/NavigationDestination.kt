package com.example.habittracker.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

// All the Navigation-Page destinations will be stored in the
data class NavigationDestination(
    val label: String,
    val icon: ImageVector
)

val destinationList = listOf(
    NavigationDestination(
        // Page for the MainScreen
        label = "Home",
        icon = Icons.Default.Home,
    ),
    NavigationDestination(
        // Page for the CustomScreen
        label = "Add",
        icon = Icons.Default.Add
    ),
    NavigationDestination(
        // Page for the HistoryScreen
        label = "History",
        icon = Icons.Default.DateRange
    )
)