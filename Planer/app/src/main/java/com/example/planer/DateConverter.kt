package com.example.planer

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateConverter
{
    //Oficjalka z dokumentacji lekko przerobiona
    @TypeConverter
    fun fromStringToDate(value: String?): Date?
    {
        return value?.let{
            SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).parse(it)
        }
    }

    @TypeConverter
    fun fromDateToString(date: Date?): String?
    {
        return date?.let {
            SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).format(it)
        }
    }
}