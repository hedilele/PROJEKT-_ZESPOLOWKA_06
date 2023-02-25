package com.example.planer.DAOs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.planer.entities.Habits

@Dao
interface HabitsDAO
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(habits: Habits)
}