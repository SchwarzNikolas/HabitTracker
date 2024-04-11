package com.example.habittracker

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


// this is the Data Access Object
@Dao
interface HabitDao{

    // defining data base operations
    @Query("SELECT * FROM Habit")
     fun fetchHabits(): Flow<List<Habit>>

    @Upsert
    suspend fun insertHabit(habit: Habit)

    @Insert
    suspend fun insertRecord(record: HabitRecord)

}