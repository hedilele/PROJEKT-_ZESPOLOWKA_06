package com.example.planer.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.planer.AppDatabase
import com.example.planer.repository.CalendarRepository
import com.example.planer.entities.Calendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CalendarViewModel(application: Application): AndroidViewModel(application)
{
    private val repository : CalendarRepository

    init
    {
        val calendarDAO = AppDatabase.getDatabase(application).calendarDAO()
        repository = CalendarRepository(calendarDAO)
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
}