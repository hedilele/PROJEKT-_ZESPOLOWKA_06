package com.example.planer.repository

import androidx.lifecycle.LiveData
import com.example.planer.DAOs.CalendarDAO
import com.example.planer.notification.NotificationHelper
import com.example.planer.entities.Calendar
import com.example.planer.entities.Notes


class CalendarRepository(private val calendarDAO: CalendarDAO,private val notificationHelper: NotificationHelper)
{
    fun readAllData() : LiveData<List<Calendar>>
    {
        val readAllData: LiveData<List<Calendar>> = calendarDAO.getAllDates()
        // Dodaj poniższy kod
        readAllData.value?.forEach { calendar ->
            notificationHelper.scheduleNotification(calendar.startDate, calendar.id,calendar.reminder,calendar.name)
        }

        return readAllData
    }
    //Dodawanie daty do kalendarza
    suspend fun addCalendarDate(calendar: Calendar)
    {
        calendarDAO.insert(calendar)
    }

    suspend fun insertEventWithNote(calendar: Calendar, note: Notes) {
        calendarDAO.insertCalendarWithNote(calendar, note)
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
    fun getAll()
    {
        calendarDAO.getAllDates()
    }

    fun getAllList(): List<Calendar> {
        return calendarDAO.getAllDatesList()
    }

    suspend fun insertEventWithNoteAndGetId(event: Calendar, note: Notes): Long {
        return calendarDAO.insertCalendarWithNoteAndGetId(event, note)
    }


}