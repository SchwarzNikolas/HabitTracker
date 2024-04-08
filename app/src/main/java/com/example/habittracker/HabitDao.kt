package com.example.habittracker

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HabitDao{

    @Query("SELECT * FROM Habit")
    suspend fun fetchHabits(): List<Habit>

    @Insert
    suspend fun insertHabit(habit: Habit)
}