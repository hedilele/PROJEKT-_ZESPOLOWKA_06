package com.example.planer.repository

import androidx.lifecycle.LiveData
import com.example.planer.DAOs.CalendarDAO
import com.example.planer.entities.Calendar


class CalendarRepository(private val calendarDAO: CalendarDAO)
{
    //Dodawanie daty do kalendarza
    suspend fun addCalendarDate(calendar: Calendar)
    {
        calendarDAO.insert(calendar)
    }

    //Rozne usuwania
    //po calym wydarzeniu
    suspend fun deleteCalendarDate(calendar: Calendar)
    {
        calendarDAO.delete(calendar)
    }

    //po id
    suspend fun deleteCalendarDateById(id: Long)
    {
        calendarDAO.deleteById(id)
    }

    //wszystkie
    suspend fun deleteAll()
    {
        calendarDAO.deleteAll()
    }

    //Update
    suspend fun updateCalendar(calendar: Calendar)
    {
        calendarDAO.update(calendar)
    }

    //Wyszukiwanie po id
    suspend fun getCalendarById(id: Long)
    {
        calendarDAO.getCalendarDateById(id)
    }

    //Wyswietlanie wszystkich
    suspend fun getAll()
    {
        calendarDAO.getAllDates()
    }
}