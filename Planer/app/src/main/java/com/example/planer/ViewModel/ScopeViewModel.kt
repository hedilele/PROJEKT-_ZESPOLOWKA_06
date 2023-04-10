package com.example.planer.ViewModel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.planer.AppDatabase
import com.example.planer.entities.Notes
import com.example.planer.entities.Tasks
import com.example.planer.repository.ScopeRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ScopeViewModel(application: Application): AndroidViewModel(application)
{
    private val taskDAO = AppDatabase.getDatabase(application).tasksDAO()
    private val scopeRepository: ScopeRepository = ScopeRepository(taskDAO)

    fun getOverdueTasks(): LiveData<Map<Tasks, List<Notes>>> {
        return scopeRepository.getOverdueTasks()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun postponeTaskTommorow(task: Tasks) {
        val currentDateTime = LocalDateTime.now().plusDays(1)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val formattedDateTime = currentDateTime.format(formatter).toString()

        task.deadline = formattedDateTime
        scopeRepository.updateTask(task)
    }

    suspend fun setTaskAsDone(task: Tasks) {
        task.isActive = 0
        scopeRepository.updateTask(task)
    }

}