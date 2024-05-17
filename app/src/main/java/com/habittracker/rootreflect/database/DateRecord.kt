package com.habittracker.rootreflect.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.LocalDate

@Entity
data class DateRecord (
    @PrimaryKey
    val key: Int = 1,
    val date: LocalDate
)

class DateConverter{
    @TypeConverter
    fun fromDateToString(date: LocalDate): String{
        return date.toString()
    }

    @TypeConverter
    fun fromStringToDate(dateString: String): LocalDate{
        return LocalDate.parse(dateString)
    }
}