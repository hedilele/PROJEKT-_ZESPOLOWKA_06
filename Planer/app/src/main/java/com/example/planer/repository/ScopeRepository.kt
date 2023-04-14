package com.example.planer.repository

import androidx.lifecycle.LiveData
import com.example.planer.DAOs.TasksDAO
import com.example.planer.entities.Notes
import com.example.planer.entities.Tasks

class ScopeRepository(private val tasksDAO: TasksDAO) {
    fun getOverdueTasks(): LiveData<Map<Tasks, List<Notes>>> {
        return tasksDAO.readOverdueTasksWithNotes()
    }

    suspend fun updateTask(task: Tasks) {
        tasksDAO.update(task)
    }

}