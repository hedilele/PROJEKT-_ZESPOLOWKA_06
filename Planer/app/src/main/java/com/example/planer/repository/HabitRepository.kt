package com.example.planer.repository

import androidx.lifecycle.LiveData
import com.example.planer.DAOs.HabitsDAO
import com.example.planer.entities.Habits

class HabitRepository(private val habitsDAO: HabitsDAO)
{

    val readAllData: LiveData<List<Habits>> = habitsDAO.readActiveHabits()
    //val readDataWithTasks: LiveData<List<Tasks>> = tasksDAO.readTasksWithTypes(typeId = Int)
    //val readP: LiveData<List<Tasks>> = tasksDAO.readP()

    suspend fun addHabit(habits: Habits)
    {
        habitsDAO.insert(habits)
    }

    suspend fun updateHabit(habits: Habits)
    {
        habitsDAO.update(habits)
    }

    suspend fun activateAllHabits() {
        habitsDAO.activateHabits()
    }

    suspend fun deleteHabit(habits: Habits)
    {
        habitsDAO.delete(habits)
    }

//    suspend fun deleteHabitById(id: Int)
//    {
//        habitsDAO.delete(habitsDAO.getHabitById(id))
//    }


    fun readAllDeadlines()
    {
        habitsDAO.readActiveHabits()
    }

    fun getHabitsList(): List<Habits> {
        return habitsDAO.getAllHabitsList()
    }

    fun readAllDataa()
    {
        habitsDAO.readActiveHabits()
    }


}