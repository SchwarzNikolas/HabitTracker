package com.example.habittracker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {

    // Fetch all Moods
    // Updates or Inserts a mood
    // Delete a mood
    @Query("SELECT * FROM Mood")
    fun fetchMoods(): Flow<List<Mood>>

    @Upsert
    suspend fun upsertMood(mood: Mood)

    @Delete
    suspend fun deleteMood(mood: Mood)

    // Fetch all MoodRecords
    // Updates or Inserts a moodRecord
    // Delete a moodRecord using the date
    @Query("SELECT * FROM MoodRecord")
    fun fetchMoodRecords(): Flow<List<MoodRecord>>

    @Upsert
    suspend fun upsertMoodRec(moodRec: MoodRecord)

    @Query("DELETE FROM HabitRecord WHERE date = :date")
    suspend fun deleteMoodRec(date: String)
}