package com.example.habittracker.database;

import androidx.room.Embedded


class HabitJoin(
        @Embedded
        val habit: Habit,
        @Embedded
        val completion: HabitCompletion
)