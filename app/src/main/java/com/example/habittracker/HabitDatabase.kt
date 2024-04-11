package com.example.habittracker

import androidx.room.Database
import androidx.room.RoomDatabase

// This is the data base
@Database(
    entities = [Habit::class, HabitRecord::class],
    version = 1
)
abstract class HabitDatabase:RoomDatabase(){
    abstract val dae:HabitDao
}