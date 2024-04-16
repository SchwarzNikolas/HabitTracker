package com.example.habittracker

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
@Dao
interface CustomHabitDao {

    // Fetches all rows in CustomHabit Table
    @Query("SELECT * FROM CustomHabit")
    fun fetchCusHabits(): Flow<List<CustomHabit>>

    // Updates &/ inserts CustomHabit
    @Upsert
    suspend fun insertCusHabit(cusHabit: CustomHabit)

    // Insert a new role to CustomHabitRecord table
    @Insert
    suspend fun insertCusHabitRecord(cusRecord: CustomHabitRecord)

    // Delete a new role to CustomHabitRecord table
    @Query("DELETE FROM CustomHabitRecord WHERE name = :name AND date = :date")
    suspend fun deleteCusHabitRecord(name: String, date:String)

    // Select everything from CustomHabitRecord table
    @Query("SELECT * FROM CustomHabitRecord")
    fun fetchCusHabitRecords(): Flow<List<CustomHabitRecord>>

    //Deletion of a row in CustomHabit table
    @Delete
    suspend fun deleteCusHabit(cusHabit: CustomHabit)



}