package com.habittracker.rootreflect.database;

import androidx.room.Embedded


class HabitJoin(
        @Embedded
        var habit: Habit,
        @Embedded
        var completion: HabitCompletion,
)