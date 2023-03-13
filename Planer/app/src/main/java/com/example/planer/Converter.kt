package com.example.planer

import androidx.room.TypeConverter
import java.util.Date

class Converter
{
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long?
    {
        return date?.time?.toLong()
    }
}