package com.top.planer.repository

import androidx.lifecycle.LiveData
import com.top.planer.DAOs.HabitsDAO
import com.top.planer.entities.Habits

class HabitRepository(private val habitsDAO: HabitsDAO)
{

    val readAllData: LiveData<List<Habits>> = habitsDAO.readActiveHabits()

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