package com.example.habittracker.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(foreignKeys = [ForeignKey(entity = Habit::class,
    parentColumns = arrayOf("habitId"),
    childColumns = arrayOf("habitID")
        )
    ]
)
data class HabitCompletion (
    @PrimaryKey(autoGenerate = true)
    val habitID: Int = 0,
    val completion: Int = 0
    )
