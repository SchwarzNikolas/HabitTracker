package com.habittracker.rootreflect.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


// this is the Data Access Object
// defining database interactions
@Dao
interface HabitDao{

    @Query("Select * from DateRecord limit 1")
    suspend fun getDate(): DateRecord

    @Upsert()
    suspend fun upsertDate(dateRecord: DateRecord)
    // defines sql query to be executed on the database
    @Query("SELECT * FROM Habit WHERE occurrence LIKE :day ORDER by done")
    fun fetchHabitByDay(day: String): Flow<List<Habit>>

    @Upsert
    suspend fun upsertHabit(habit: Habit)
    @Insert
    suspend fun insertHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("UPDATE Habit SET completion = 0, done = 'false'")
    suspend fun resetCompletion()

    @Query("SELECT * FROM HabitRecord WHERE date = :date")
    fun fetchHabitRecordsByDate(date: LocalDate):Flow<List<HabitRecord>>

    @Upsert
    suspend fun upsertRecord(record: HabitRecord)

    // defines sql query to be executed on the database
    @Query("DELETE FROM HabitRecord WHERE habitName = :name AND date = :date")
    suspend fun deleteRecord(name: String, date: LocalDate)

    // Get the moodRec by the date
    @Query("SELECT * FROM MoodRecord WHERE moodDate = :date LIMIT 1")
    suspend fun getMoodRecByDate(date: LocalDate): MoodRecord?

    // Updates or Inserts a moodRecord
    @Upsert
    suspend fun upsertMoodRec(moodRec: MoodRecord)

    @Query("select moodDate from MoodRecord")
    fun fetchDates():Flow<List<LocalDate>>
    // for debugging
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMoodRecordDebug(entity: MoodRecord)
}
