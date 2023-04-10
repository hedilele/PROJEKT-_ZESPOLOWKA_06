package com.example.planer.repository

import androidx.lifecycle.LiveData
import com.example.planer.DAOs.TasksDAO
import com.example.planer.entities.Notes
import com.example.planer.entities.Tasks
import com.example.planer.entities.relations.NoteAndTask

class ScopeRepository(private val tasksDAO: TasksDAO)
{
    fun getOverdueTasks(): LiveData<Map<Tasks, List<Notes>>> {
        return tasksDAO.readOverdueTasksWithNotes()
    }

    fun getAllTasks(): LiveData<List<NoteAndTask>> {
        return tasksDAO.readAllDataWithNotes()
    }

}