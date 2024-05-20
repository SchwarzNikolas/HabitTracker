package com.habittracker.rootreflect.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// This is the database
@Database(
    // defines which entities belong to the database
    entities = [
        Habit::class,
        HabitRecord::class,
        MoodRecord::class,
        DateRecord::class],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class HabitDatabase:RoomDatabase(){
    // defines which daos belongs to this database
    abstract val dao: HabitDao
}