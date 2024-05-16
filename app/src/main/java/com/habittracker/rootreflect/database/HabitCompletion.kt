package com.habittracker.rootreflect.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(foreignKeys = [ForeignKey(entity = Habit::class,
    parentColumns = arrayOf("name"),
    childColumns = arrayOf("habitName"),
    onDelete = 5
        )
    ]
)
data class HabitCompletion (
    @PrimaryKey
    val habitName: String = "",
    val completion: Int = 0,
    val done: Boolean = false,
    val occurrence: String = "1111111"
    )
