package com.example.planer.repository

import androidx.lifecycle.LiveData
import com.example.planer.DAOs.TasksDAO
import com.example.planer.entities.relations.NoteAndTask

class ScopeRepository(private val tasksDAO: TasksDAO)
{
    fun getOverdueTasks(): LiveData<List<NoteAndTask>> {
        return tasksDAO.readOverdueTasksWithNotes()
    }

}