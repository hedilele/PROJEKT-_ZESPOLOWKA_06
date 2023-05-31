package com.top.planer.repository

import androidx.lifecycle.LiveData
import com.top.planer.DAOs.CalendarDAO
import com.top.planer.notification.NotificationHelper
import com.top.planer.entities.Calendar
import com.top.planer.entities.Notes


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
    suspend fun deleteCalendarDateById(id: Long, type: Int)
    {
        calendarDAO.deleteById(id) // po prostu usuwa wydarzenie
        //if (type!=0)
        if (type > 0){ calendarDAO.deleteByType(type)
            calendarDAO.deleteById(type.toLong())} // usunie pozostałe z serii i parent wydarzenie
        if (type < 0){ calendarDAO.deleteByType(id.toInt()) } // usunie dzieci wydarzenia parent


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

    suspend fun insertMapOfEvents(map: Map<Calendar, Notes>) {
        calendarDAO.insertMapOfCalendarWithNote(map)
    }

    suspend fun deleteICalEvents(calendarList: List<Calendar>) {
        calendarDAO.deleteFromICalList(calendarList)
    }


}