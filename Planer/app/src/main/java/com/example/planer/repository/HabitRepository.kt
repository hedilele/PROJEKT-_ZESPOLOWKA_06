package com.example.planer.repository

import androidx.lifecycle.LiveData
import com.example.planer.DAOs.HabitsDAO
import com.example.planer.DAOs.TasksDAO
import com.example.planer.entities.Habits
import com.example.planer.entities.Tasks

class HabitRepository(private val habitsDAO: HabitsDAO)
{

    val readAllData: LiveData<List<Habits>> = habitsDAO.readAllData()
    //val readDataWithTasks: LiveData<List<Tasks>> = tasksDAO.readTasksWithTypes(typeId = Int)
    //val readP: LiveData<List<Tasks>> = tasksDAO.readP()

    suspend fun addTask(habits: Habits)
    {
        habitsDAO.insert(habits)
    }

    suspend fun deleteTask(habits: Habits)
    {
        habitsDAO.delete(habits)
    }

    fun readAllDeadlines()
    {
        habitsDAO.readAllData()
    }

    suspend fun readAllDataa()
    {
        habitsDAO.readAllData()
    }


}