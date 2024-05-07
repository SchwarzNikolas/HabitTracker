package com.example.habittracker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.habittracker.mood.MoodType
import kotlinx.coroutines.flow.Flow


// this is the Data Access Object
// defining database interactions
@Dao
interface HabitDao{

    // defines sql query to be executed on the database
    @Query("SELECT * FROM Habit JOIN HabitCompletion ON Habit.habitId = HabitCompletion.habitID WHERE HabitCompletion.occurrence LIKE :day")
    fun fetchHabitByDay(day: String): Flow<List<HabitJoin>>
    @Upsert
    suspend fun insertHabit(habit: Habit)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Insert
    suspend fun insertCompletion(habitCompletion: HabitCompletion)

    @Update
    suspend fun updateCompletion(completion: HabitCompletion)
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
    suspend fun deleteRecord(name: String, date:String)



    // Fetch all MoodRecords
    // Updates or Inserts a moodRecord
    @Query("SELECT * FROM MoodRecord")
    fun fetchMoodRecords(): Flow<List<MoodRecord>>

    @Query("SELECT * FROM MoodRecord WHERE moodDate = :date LIMIT 1")
    suspend fun getMoodRecByDate(date: String): MoodRecord?

    /*@Transaction
    suspend fun upsertMoodRec(moodRec: MoodRecord) {
        val existingRecord = getMoodRecByDate(moodRec.moodDate)
        if (existingRecord == null) {
            insertMoodRec(moodRec)
        } else {
            val updatedRecord = existingRecord.copy(moodRecId = moodRec.moodRecId)
            insertMoodRec(updatedRecord)
        }
    }*/

    @Upsert
    suspend fun insertMoodRec(moodRec: MoodRecord)

    @Query("UPDATE MoodRecord SET mood = :mood WHERE moodDate = :date")
    suspend fun updateMoodRec(date: String, mood: MoodType)

    @Query("DELETE FROM MoodRecord WHERE moodDate = :date")
    suspend fun deleteMoodRecord(date:String)
}