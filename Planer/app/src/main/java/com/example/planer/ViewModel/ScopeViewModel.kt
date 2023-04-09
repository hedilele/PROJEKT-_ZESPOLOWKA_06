package com.example.planer.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.planer.AppDatabase
import com.example.planer.entities.relations.NoteAndTask
import com.example.planer.repository.ScopeRepository


class ScopeViewModel(application: Application): AndroidViewModel(application)
{
    private val taskDAO = AppDatabase.getDatabase(application).tasksDAO()
    private val scopeRepository: ScopeRepository = ScopeRepository(taskDAO)

    fun getAllTasksDebug(): LiveData<List<NoteAndTask>> {
        return scopeRepository.getAllTasks()
    }

    fun getOverdueTasks(): LiveData<List<NoteAndTask>> {
        return scopeRepository.getOverdueTasks()
    }

}