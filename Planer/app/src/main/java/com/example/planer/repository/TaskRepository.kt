package com.example.planer.repository

import androidx.lifecycle.LiveData
import com.example.planer.DAOs.TasksDAO
import com.example.planer.entities.Tasks
import java.text.SimpleDateFormat

class TaskRepository(private val tasksDAO: TasksDAO)
{

    val readAllData: LiveData<List<Tasks>> = tasksDAO.readAllData()
    //val readDataWithTasks: LiveData<List<Tasks>> = tasksDAO.readTasksWithTypes(typeId = Int)
    //val readP: LiveData<List<Tasks>> = tasksDAO.readP()

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

    suspend fun updateTask(task: Tasks)
    {
        tasksDAO.update(task)
    }

    suspend fun readAllDataa()
    {
        tasksDAO.readAllDataa()
    }

    //Czytwanie po typach
    fun readTasksWithTypes(typeIds: List<Int>): LiveData<List<Tasks>>
    {
        return tasksDAO.readTasksWithTypes(typeIds)
    }

    //Czytwanie po czasie trwania
    fun readTasksWithDuration(timeToFinishes: List<Int>): LiveData<List<Tasks>>
    {
        return tasksDAO.readTasksWithDuration(timeToFinishes)
    }

    //Czytanie po dacie
    fun readTasksWithDates(startDate: String, endDate: String): LiveData<List<Tasks>>
    {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val startMillis = sdf.parse(startDate)?.time ?: 0L
        val endMillis = sdf.parse(endDate)?.time ?: 0L
        return tasksDAO.readTasksWithTime(startDate, endDate)
    }

    //Czytwanie po typach i czasie trwania
    fun readTasksWithTypesAndDuration(typeIds: List<Int>,timeToFinishes: List<Int>): LiveData<List<Tasks>>
    {
        return tasksDAO.readTasksWithTypesAndDurations(typeIds,timeToFinishes)
    }
}