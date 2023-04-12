package com.example.planer.repository

import androidx.lifecycle.LiveData
import com.example.planer.DAOs.HabitsDAO
import com.example.planer.DAOs.TasksDAO
import com.example.planer.entities.Habits
import com.example.planer.entities.Tasks

class HabitRepository(private val habitsDAO: HabitsDAO)
{

    val readAllData: List<Habits> = habitsDAO.getAllHabits()

    suspend fun addTask(habit: Habits)
    {
        habitsDAO.insert(habit)
    }

    suspend fun deleteTask(habit: Habits)
    {
        habitsDAO.delete(habit)
    }

    suspend fun deleteTaskById(id: Int)
    {
        habitsDAO.delete(habitsDAO.getHabitById(0))

    }

    fun readAllDeadlines()
    {
        habitsDAO.getAllHabits()
    }

    suspend fun updateTask(habit: Habits)
    {
        habitsDAO.update(habit)
    }
}