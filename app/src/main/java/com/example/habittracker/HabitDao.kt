package com.example.habittracker

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface HabitDao{

    @Query("SELECT * FROM Habit")
    suspend fun fetchHabits(): List<Habit>

    @Upsert
    suspend fun insertHabit(habit: Habit)

    @Insert
    suspend fun insertRecord(record: HabitRecord)

}