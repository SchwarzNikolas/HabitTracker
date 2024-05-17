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
    @Query("SELECT * FROM Habit JOIN HabitCompletion ON Habit.habitId = HabitCompletion.habitID WHERE HabitCompletion.occurrence LIKE :day")
    fun fetchHabitByDay(day: String): Flow<List<HabitJoin>>

    @Upsert
    suspend fun upsertHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Upsert
    suspend fun upsertCompletion(habitCompletion: HabitCompletion)

    @Query("UPDATE HabitCompletion SET completion = 0, done = 'false'")
    suspend fun resetCompletion()
    @Delete
    suspend fun deleteCompletion(completion: HabitCompletion)

    @Query("SELECT * FROM HabitRecord")
    fun fetchHabitRecords():Flow<List<HabitRecord>>

    @Query("SELECT * FROM HabitRecord WHERE date = :date")
    fun fetchHabitRecordsByDate(date: LocalDate):Flow<List<HabitRecord>>

    @Insert
    suspend fun insertRecord(record: HabitRecord)

    // defines sql query to be executed on the database
    @Query("DELETE FROM HabitRecord WHERE habitName = :name AND date = :date")
    suspend fun deleteRecord(name: String, date: LocalDate)

    // Get the moodRec by the date
    @Query("SELECT * FROM MoodRecord WHERE moodDate = :date LIMIT 1")
    suspend fun getMoodRecByDate(date: String): MoodRecord?

    // Updates or Inserts a moodRecord
    @Upsert
    suspend fun upsertMoodRec(moodRec: MoodRecord)

    @Query("select moodDate from MoodRecord")
    fun fetchDates():Flow<List<LocalDate>>

    // dor debugging
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMoodRecordDebug(entity: MoodRecord)

    // for testing
    @Query("SELECT * FROM Habit")
    fun fetchHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM HabitCompletion")
    fun getHabit(): Flow<List<HabitCompletion>>

    @Upsert
    fun updateHabit(habit: Habit)

    @Query("SELECT * FROM MoodRecord")
    fun fetchMoodRecords(): Flow<List<MoodRecord>>

}
