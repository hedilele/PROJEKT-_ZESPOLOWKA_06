package com.example.planer

import androidx.room.TypeConverter
import java.util.Date

class DateConverter
{
    //Oficjalka z dokumentacji lekko przerobiona
    @TypeConverter
    fun fromTimestamp(value: Long?): Date?
    {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long?
    {
        return date?.time?.toLong()
    }
}