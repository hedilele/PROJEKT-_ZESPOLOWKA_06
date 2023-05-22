package com.top.planer.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.top.planer.AppDatabase
import com.top.planer.entities.Habits
import com.top.planer.entities.LastAccess
import com.top.planer.repository.HabitRepository
import com.top.planer.repository.LastAccessRepository
import kotlinx.coroutines.*
import java.time.LocalDate

//Referencja do aplikacji
class HabitViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<Habits>>
    val lastAccessDate: LiveData<LastAccess>
    private val repository: HabitRepository
    private val lastAccess: LastAccessRepository

    //To zawsze pierwsze bedzie sie wykonywalo kiedy callujemy UserViewModel
    init {
        val habitDAO = AppDatabase.getDatabase(application).habitsDAO()
        val lastAccessDAO = AppDatabase.getDatabase(application).lastAccessDAO()
        repository = HabitRepository(habitDAO)
        lastAccess = LastAccessRepository(lastAccessDAO)
        readAllData = repository.readAllData
        lastAccessDate = lastAccess.getLastAccessDate()
    }

    //Zla praktyka jest uruchamiac zapytania z bazy w watku glownym!
    fun addHabit(habit: Habits) {
        viewModelScope.launch(Dispatchers.IO) //Odpali sie w oddzielnym watku w tle
        {
            repository.addHabit(habit)
        }
    }

    fun deleteHabit(habit: Habits) {
        viewModelScope.launch(Dispatchers.IO) //Odpali sie w oddzielnym watku w tle
        {
            repository.deleteHabit(habit)
        }
    }

    fun updateHabit(habit: Habits) {
        viewModelScope.launch(Dispatchers.IO) //Odpali sie w oddzielnym watku w tle
        {
            repository.updateHabit(habit)
        }
    }


    fun updateLastAccessDate() {
        viewModelScope.launch(Dispatchers.IO)
        {
            lastAccess.updateAccessDate(LocalDate.now())
        }
    }

    fun activateHabits() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.activateAllHabits()
        }
    }

    fun createLastAccess() {
        viewModelScope.launch(Dispatchers.IO)
        {
            lastAccess.createLastAccess(LocalDate.now())
        }
    }

    fun getHabitsList(): List<Habits> {

        return repository.getHabitsList()
    }


    fun readAllData() {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.readAllDataa()
        }
    }

}