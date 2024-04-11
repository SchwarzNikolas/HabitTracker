package com.example.habittracker

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao{

    @Query("SELECT * FROM Habit")
     fun fetchHabits(): Flow<List<Habit>>

    @Upsert
    suspend fun insertHabit(habit: Habit)

    @Insert
    suspend fun insertRecord(record: HabitRecord)

}