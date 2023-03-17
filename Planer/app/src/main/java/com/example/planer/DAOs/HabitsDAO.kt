package com.example.planer.DAOs

import androidx.room.*
import com.example.planer.entities.Habits

@Dao
interface HabitsDAO
{
    //Insertowanie jakiegos habitsa do bazy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(habits: Habits)

    //Updatowanie danych
    @Update
    suspend fun update(habits: Habits)

    //Pobieranie listy habitsow z bazy
    @Query("SELECT * FROM habits")
    suspend fun getAllHabits(): List<Habits>

    //Pobieranie Habitsa po id
    @Query("SELECT * FROM habits WHERE id = :habitId")
    suspend fun getHabitById(habitId: Int): Habits //TODO - zwracac ma liste czy pojedynczy

    //Usuwanie po parametrach
    @Delete
    suspend fun delete(habits: Habits)

    //Usuwanie wszystkiego
    @Query("DELETE FROM habits")
    suspend fun deleteAll()


}