package com.habittracker.rootreflect.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.habittracker.rootreflect.mood.MoodType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


// this is the Data Access Object
// defining database interactions
@Dao
interface HabitDao{

    @Query("Select * from DateRecord limit 1")
    suspend fun getDate(): DateRecord

    @Upsert
    suspend fun upsertMoodRec(moodRec: MoodRecord)
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

    @Insert
    suspend fun insertRecord(record: HabitRecord)

    // defines sql query to be executed on the database
    @Query("DELETE FROM HabitRecord WHERE habitName = :name AND date = :date")
    suspend fun deleteRecord(name: String, date: LocalDate)



    // Fetch all MoodRecords
    // Updates or Inserts a moodRecord
    @Query("SELECT * FROM MoodRecord")
    fun fetchMoodRecords(): Flow<List<MoodRecord>>

    @Query("SELECT * FROM MoodRecord WHERE moodDate = :date LIMIT 1")
    suspend fun getMoodRecByDate(date: String): MoodRecord?



    @Upsert
    suspend fun insertMoodRec(moodRec: MoodRecord)

    @Query("UPDATE MoodRecord SET mood = :mood WHERE moodDate = :date")
    suspend fun updateMoodRec(date: String, mood: MoodType)

    // debug
    @Query("insert into MoodRecord (moodDate, mood) values (:date, :mood)")
    suspend fun debugCalendar(date: LocalDate, mood: MoodType)

    @Query("select moodDate from MoodRecord")
    fun fetchDates():Flow<List<LocalDate>>

}