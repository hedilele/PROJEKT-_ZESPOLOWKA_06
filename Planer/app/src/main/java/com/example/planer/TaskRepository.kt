package com.example.planer

import androidx.lifecycle.LiveData
import com.example.planer.DAOs.TasksDAO
import com.example.planer.entities.Tasks

class TaskRepository(private val tasksDAO: TasksDAO)
{

    val readAllData: LiveData<List<Tasks>> = tasksDAO.readAllData()

    suspend fun addTask(tasks: Tasks)
    {
        tasksDAO.insert(tasks)
    }

    suspend fun deleteTask(tasks: Tasks)
    {
        tasksDAO.delete(tasks)
    }

    suspend fun deleteTaskById(id: Int)
    {
        tasksDAO.delete(tasksDAO.findTaskById(id)[0])

    }

    fun readAllDeadlines()
    {
        tasksDAO.readAllDeadlines()
    }
}