package com.top.planer.ViewModel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.top.planer.AppDatabase
import com.top.planer.notification.NotificationHelper
import com.top.planer.entities.Calendar
import com.top.planer.entities.Notes
import com.top.planer.repository.CalendarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CalendarViewModel(application: Application): AndroidViewModel(application)
{
    val getAll: LiveData<List<Calendar>>
    private val repository : CalendarRepository
    private val notificationHelper: NotificationHelper = NotificationHelper(application)

    init
    {
        val calendarDAO = AppDatabase.getDatabase(application).calendarDAO()
        repository = CalendarRepository(calendarDAO, notificationHelper)
        getAll = repository.readAllData()
        getAll.observeForever { calendars ->
            calendars?.forEach { calendar ->
                notificationHelper.scheduleNotification(calendar.startDate, calendar.id,calendar.reminder,calendar.name)
            }
        }

    }
    fun addCalendarDate(calendar: Calendar)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.addCalendarDate(calendar)
        }
    }


    fun deleteCalendarDate(calendar: Calendar)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.deleteCalendarDate(calendar)
        }
    }

    fun deleteCalendarDateById(id : Long)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.deleteCalendarDateById(id)
        }
    }

    fun deleteAll()
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.deleteAll()
        }
    }

    fun updateCalendar(calendar: Calendar)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.updateCalendar(calendar)
        }
    }

    fun getCalendarById(id : Long)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.getCalendarById(id)
        }
    }

    fun getAll()
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.getAll()
        }
    }

    fun insertEventWithNoteAndGetId(event: Calendar, note: Notes, callback: (Long) -> Unit) {
        viewModelScope.launch {
            val eventId = repository.insertEventWithNoteAndGetId(event, note)
            callback(eventId)
        }
    }

    fun insertEventWithNote(calendar: Calendar, note:Notes)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.insertEventWithNote(calendar, note)
        }
    }
}