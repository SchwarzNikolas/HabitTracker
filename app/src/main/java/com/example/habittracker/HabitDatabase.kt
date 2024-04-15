package com.example.habittracker

import androidx.room.Database
import androidx.room.RoomDatabase

// This is the database
@Database(
    // defines which entities belong to the database
    entities = [Habit::class, HabitRecord::class],
    version = 1
)
abstract class HabitDatabase:RoomDatabase(){
    // defines which dao belongs to this database
    abstract val dao:HabitDao
}