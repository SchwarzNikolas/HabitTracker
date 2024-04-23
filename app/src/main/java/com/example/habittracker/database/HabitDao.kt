package com.example.habittracker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


// this is the Data Access Object
// defining database interactions
@Dao
interface HabitDao{

    // defines sql query to be executed on the database
    @Query("SELECT * FROM Habit")
     fun fetchHabits(): Flow<List<Habit>>

     // updates or inserts habit
    @Upsert
    suspend fun insertHabit(habit: Habit)

    @Insert
    suspend fun insertRecord(record: HabitRecord)

    // defines sql query to be executed on the database
    @Query("DELETE FROM HabitRecord WHERE habitName = :name AND date = :date")
    suspend fun deleteRecord(name: String, date:String)

    // defines sql query to be executed on the database
    @Query("SELECT * FROM HabitRecord")
    fun fetchHabitRecords():Flow<List<HabitRecord>>

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Delete
    suspend fun deleteCompletion(completion: HabitCompletion)

    @Query("SELECT * FROM Habit JOIN HabitCompletion ON Habit.habitId = HabitCompletion.habitID")
     fun getHabit(): Flow<List<HabitJoin>>

     @Insert
     suspend fun insertCompletion(habitCompletion: HabitCompletion)

     @Update
     suspend fun updateCompletion(completion: HabitCompletion)

     @Query("UPDATE HabitCompletion SET completion = 0")
     suspend fun resetCompletion()
}