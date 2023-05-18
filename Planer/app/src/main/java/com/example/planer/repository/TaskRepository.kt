package com.example.planer.repository

import androidx.lifecycle.LiveData
import com.example.planer.DAOs.TasksDAO
import com.example.planer.norification.NotificationHelper
import com.example.planer.entities.Notes
import com.example.planer.entities.Tasks
import java.text.SimpleDateFormat

class TaskRepository(private val tasksDAO: TasksDAO,private val notificationHelper: NotificationHelper)
{
    fun readAllData() : LiveData<List<Tasks>>
    {
        val readAllData: LiveData<List<Tasks>> = tasksDAO.readAllData()
        // Dodaj poniższy kod
        readAllData.value?.forEach { task ->
            //notificationHelper.scheduleNotification(task.deadline, task.id, task.title)
        }

        return readAllData
    }
    //Metoda do parsowania
    fun parsing(startDate: String, endDate: String)
    {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val startMillis = sdf.parse(startDate)?.time ?: 0L
        val endMillis = sdf.parse(endDate)?.time ?: 0L
    }
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
        parsing(startDate,endDate)
        return tasksDAO.readTasksWithTime(startDate, endDate)
    }

    //Czytwanie po typach i czasie trwania
    fun readTasksWithTypesAndDuration(typeIds: List<Int>,timeToFinishes: List<Int>): LiveData<List<Tasks>>
    {
        return tasksDAO.readTasksWithTypesAndDurations(typeIds,timeToFinishes)
    }

    //Czytanie po czasie trwania i dacie
    fun readTasksWithDurationAndTime(timeToFinishes: List<Int>,startDate: String, endDate: String): LiveData<List<Tasks>>
    {
        parsing(startDate,endDate)
        return tasksDAO.readTasksWithDurationAndTime(timeToFinishes,startDate,endDate)
    }

    //Czytanie po czasie i typie
    fun readTasksWithTypesAndTime(typeIds: List<Int>,startDate: String, endDate: String): LiveData<List<Tasks>>
    {
        parsing(startDate,endDate)
        return tasksDAO.readTasksWithDurationAndTime(typeIds,startDate,endDate)
    }

    //Czytanie po wszystkich 3 parametrach
    fun readTasksWithTypesAndDurationAndTime(typeIds: List<Int>,timeToFinishes: List<Int>,startDate: String, endDate: String): LiveData<List<Tasks>>
    {
        parsing(startDate,endDate)
        return tasksDAO.readTasksWithTypesAndTimeAndDuration(typeIds,timeToFinishes,startDate,endDate)
    }

    //Czytanie z searcha
    fun readTasksWithSearchEdit(searchName: String): LiveData<List<Tasks>>
    {
        return tasksDAO.readTasksWithSearchName(searchName)
    }

    suspend fun insertTaskWithNote(task: Tasks, note: Notes) {
        return tasksDAO.insertTaskWithNote(task, note)
    }

    fun getTasksList(): List<Tasks> {
        return tasksDAO.getTasksList()
    }

}