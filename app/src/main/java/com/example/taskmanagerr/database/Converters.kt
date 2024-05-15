package com.example.taskmanagerr.database

import androidx.room.TypeConverter
import java.sql.Date
import java.sql.Time

// Converters class is a class that converts Date objects to Long and vice versa.
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? { // Converts a Long value to a Date object
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? { // Converts a Date object to a Long value
        return date?.time
    }
    @TypeConverter
    fun fromTime(value: Long?): Time? { // Converts a Long value to a Time object
        return value?.let { Time(it) }
    }

    @TypeConverter
    fun timeToTimestamp(time: Time?): Long? { // Converts a Time object to a Long value
        return time?.time
    }

}