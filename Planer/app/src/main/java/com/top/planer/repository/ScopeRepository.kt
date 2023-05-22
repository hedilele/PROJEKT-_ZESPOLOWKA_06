package com.top.planer.repository

import androidx.lifecycle.LiveData
import com.top.planer.DAOs.TasksDAO
import com.top.planer.entities.Notes
import com.top.planer.entities.Tasks

class ScopeRepository(private val tasksDAO: TasksDAO) {
    fun getOverdueTasks(): LiveData<Map<Tasks, List<Notes>>> {
        return tasksDAO.readOverdueTasksWithNotes()
    }

    suspend fun updateTask(task: Tasks) {
        tasksDAO.update(task)
    }

    suspend fun removeTask(task: Tasks) {
        tasksDAO.delete(task)
    }

}