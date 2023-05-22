package com.top.planer.DAOs

import androidx.lifecycle.LiveData
import androidx.room.*
import com.top.planer.entities.Habits

@Dao
interface HabitsDAO
{
    //Insertowanie jakiegos habitsa do bazy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(habits: Habits)

    //Updatowanie danych
    @Update
    suspend fun update(habits: Habits)

    @Query("UPDATE Habits SET is_active=1")
    suspend fun activateHabits()

    //Pobieranie listy habitsow z bazy
    @Query("SELECT * FROM 'Habits'")
    fun getAllHabits(): LiveData<List<Habits>>

    //Pobieranie listy habitsow z bazy
    @Query("SELECT * FROM 'Habits'")
    fun getAllHabitsList(): List<Habits>

    //Zwraca tylko aktywne habitsy
    //@Query("SELECT * FROM `Habits` WHERE is_active = 1")
    @Query("SELECT * FROM 'Habits'")
    fun readActiveHabits(): LiveData<List<Habits>>

    //Usuwanie po parametrach
    @Delete
    suspend fun delete(habits: Habits)

    @Query("DELETE FROM Habits WHERE id=:id")
    suspend fun deleteById(id: Long)

    //Usuwanie wszystkiego
    @Query("DELETE FROM habits")
    suspend fun deleteAll()


}