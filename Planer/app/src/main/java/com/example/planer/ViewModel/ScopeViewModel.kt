package com.example.planer.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.planer.AppDatabase
import com.example.planer.entities.relations.NoteAndTask
import com.example.planer.repository.ScopeRepository


class ScopeViewModel(application: Application): AndroidViewModel(application)
{
    val overdueTasks: LiveData<List<NoteAndTask>>

    init
    {
        val taskDAO = AppDatabase.getDatabase(application).tasksDAO()
        val scopeRepository = ScopeRepository(taskDAO)
        overdueTasks = scopeRepository.getOverdueTasks()

    }

}