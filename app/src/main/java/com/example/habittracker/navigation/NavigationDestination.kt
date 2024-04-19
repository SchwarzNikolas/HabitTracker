package com.example.habittracker.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.habittracker.CustomScreen

data class NavigationDestination(
    val label: String,
    val icon: ImageVector
)

val destinationList = listOf(
    NavigationDestination(
        label = "Home",
        icon = Icons.Default.Home,
    ),
    NavigationDestination(
        label = "Add",
        icon = Icons.Default.Add
    ),
    NavigationDestination(
        label = "History",
        icon = Icons.Default.DateRange
    )
)