package com.example.habittracker

import androidx.room.Dao
import androidx.room.Delete
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

    @Query("DELETE FROM HabitRecord WHERE habitName = :name AND date = :date")
    suspend fun deleteRecord(name: String, date:String)

    @Query("SELECT * FROM HabitRecord")
    fun fetchHabitRecords():Flow<List<HabitRecord>>

    @Delete
    suspend fun deleteHabit(habit: Habit)

}