package com.example.planer.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.planer.AppDatabase
import com.example.planer.repository.TaskRepository
import com.example.planer.entities.Tasks
import kotlinx.coroutines.*

//Referencja do aplikacji
class TaskViewModel(application: Application): AndroidViewModel(application)
{
    val readAllData: LiveData<List<Tasks>>
    private val repository: TaskRepository

    //To zawsze pierwsze bedzie sie wykonywalo kiedy callujemy UserViewModel
    init
    {
        val taskDAO = AppDatabase.getDatabase(application).tasksDAO()
        repository = TaskRepository(taskDAO)
        readAllData = repository.readAllData
    }

    //Zla praktyka jest uruchamiac zapytania z bazy w watku glownym!
    fun addTask(tasks: Tasks)
    {
        viewModelScope.launch(Dispatchers.IO) //Odpali sie w oddzielnym watku w tle
        {
            repository.addTask(tasks)
        }
    }

    fun deleteTask(tasks: Tasks)
    {
        viewModelScope.launch(Dispatchers.IO) //Odpali sie w oddzielnym watku w tle
        {
            repository.deleteTask(tasks)
        }
    }

    fun deleteTaskById(id: Int)
    {
        viewModelScope.launch(Dispatchers.IO) //Odpali sie w oddzielnym watku w tle
        {
            repository.deleteTaskById(id)
        }
    }

    fun readDeadlines()
    {
        viewModelScope.launch(Dispatchers.IO) //Odpali sie w oddzielnym watku w tle
        {
            repository.readAllDeadlines()
        }
    }

    fun updateTask(task: Tasks)
    {
        viewModelScope.launch(Dispatchers.IO) //Odpali sie w oddzielnym watku w tle
        {
            repository.updateTask(task)
        }
    }

    fun readAllData()
    {
        viewModelScope.launch (Dispatchers.IO )
        {
            repository.readAllDataa()
        }
    }

    //Po typie
    suspend fun readTasksWithTypes(typeIds: List<Int>) : LiveData<List<Tasks>>
    {
        return withContext(Dispatchers.IO)
        {
            repository.readTasksWithTypes(typeIds)
        }
    }

    //Po czasie trwania
    suspend fun readTasksWithDuration(timeToFinishes: List<Int>): LiveData<List<Tasks>>
    {
        return withContext(Dispatchers.IO)
        {
            repository.readTasksWithDuration(timeToFinishes)
        }
    }

    //Po dacie
    suspend fun readTasksWithDate(startDate: String, endDate: String): LiveData<List<Tasks>>
    {
        return withContext(Dispatchers.IO)
        {
            repository.readTasksWithDates(startDate, endDate)
        }
    }

    //Po tym i po tym
    suspend fun readTasksWithTypesAndDuration(typeIds: List<Int>,timeToFinishes: List<Int>): LiveData<List<Tasks>>
    {
        return withContext(Dispatchers.IO)
        {
            repository.readTasksWithTypesAndDuration(typeIds, timeToFinishes)
        }
    }

    //Czytanie po czasie trwania i dacie
    suspend fun readTasksWithDurationAndDate(timeToFinishes: List<Int>,startDate: String, endDate: String): LiveData<List<Tasks>>
    {
        return withContext(Dispatchers.IO)
        {
            repository.readTasksWithDurationAndTime(timeToFinishes,startDate,endDate)
        }
    }

    //Czytanie po typie i dacie
    suspend fun readTasksWithTypesAndDate(typeIds: List<Int>, startDate: String, endDate: String): LiveData<List<Tasks>>
    {
        return withContext(Dispatchers.IO)
        {
            repository.readTasksWithTypesAndTime(typeIds, startDate, endDate)
        }
    }

    //Czytanie po wszystkim
    suspend fun readTasksWithTypesAndDurationAndDate(typeIds: List<Int>,timeToFinishes: List<Int>, startDate: String, endDate: String): LiveData<List<Tasks>>
    {
        return withContext(Dispatchers.IO)
        {
            repository.readTasksWithTypesAndDurationAndTime(typeIds,timeToFinishes,startDate,endDate)
        }
    }

    suspend fun readTasksWithSearchEdit(searchName: String): LiveData<List<Tasks>>
    {
        return withContext(Dispatchers.IO)
        {
            repository.readTasksWithSearchEdit(searchName)
        }
    }
}