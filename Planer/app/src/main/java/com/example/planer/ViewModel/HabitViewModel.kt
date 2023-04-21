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
    fun addHabit(habit: Habits)
    {
        viewModelScope.launch(Dispatchers.IO) //Odpali sie w oddzielnym watku w tle
        {
            repository.addHabit(habit)
        }
    }

    fun deleteHabit(habit: Habits)
    {
        viewModelScope.launch(Dispatchers.IO) //Odpali sie w oddzielnym watku w tle
        {
            repository.deleteHabit(habit)
        }
    }

    fun updateHabit(habit: Habits)
    {
        viewModelScope.launch(Dispatchers.IO) //Odpali sie w oddzielnym watku w tle
        {
            repository.updateHabit(habit)
        }
    }


//    fun deleteHabitById(id: Int)
//    {
//        viewModelScope.launch(Dispatchers.IO) //Odpali sie w oddzielnym watku w tle
//        {
//            repository.deleteHabitById(id)
//        }
//    }


    fun readAllData()
    {
        viewModelScope.launch (Dispatchers.IO )
        {
            repository.readAllDataa()
        }
    }

}