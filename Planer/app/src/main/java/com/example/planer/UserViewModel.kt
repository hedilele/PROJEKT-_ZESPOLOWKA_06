package com.example.planer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.planer.entities.Tasks
import com.example.planer.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//Referencja do aplikacji
class UserViewModel(application: Application): AndroidViewModel(application)
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

}