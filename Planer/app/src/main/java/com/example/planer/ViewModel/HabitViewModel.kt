package com.example.planer.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.planer.AppDatabase
import com.example.planer.entities.Habits
import com.example.planer.repository.HabitRepository
import kotlinx.coroutines.*

//Referencja do aplikacji
class HabitViewModel(application: Application): AndroidViewModel(application)
{
    val readAllData: LiveData<List<Habits>>
    private val repository: HabitRepository

    //To zawsze pierwsze bedzie sie wykonywalo kiedy callujemy UserViewModel
    init
    {
        val habitDAO = AppDatabase.getDatabase(application).habitsDAO()
        repository = HabitRepository(habitDAO)
        readAllData = repository.readAllData
    }

    //Zla praktyka jest uruchamiac zapytania z bazy w watku glownym!
    fun addTask(habit: Habits)
    {
        viewModelScope.launch(Dispatchers.IO) //Odpali sie w oddzielnym watku w tle
        {
            repository.addTask(habit)
        }
    }

    fun deleteTask(habit: Habits)
    {
        viewModelScope.launch(Dispatchers.IO) //Odpali sie w oddzielnym watku w tle
        {
            repository.deleteTask(habit)
        }
    }


    fun readAllData()
    {
        viewModelScope.launch (Dispatchers.IO )
        {
            repository.readAllDataa()
        }
    }

}