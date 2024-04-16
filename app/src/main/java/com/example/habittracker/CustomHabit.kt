package com.example.habittracker

import androidx.room.Entity
import androidx.room.PrimaryKey

//Database table for custom (weekly) habit
@Entity
data class CustomHabit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val isWeekly: Boolean = true,
    val name: String = "badminton",
    val frequency: Int = 2
)
