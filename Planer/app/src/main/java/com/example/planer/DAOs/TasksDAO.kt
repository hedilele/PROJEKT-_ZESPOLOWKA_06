package com.example.planer.DAOs

import androidx.room.*
import com.example.planer.entities.Tasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDAO
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tasks: Tasks)

    @Update
    suspend fun update(tasks: Tasks)

    @Delete
    suspend fun delete(tasks: Tasks)

    @Query("SELECT * FROM `tasks`")
    fun fetchAll(): Flow<List<Tasks>>

    @Query("SELECT * FROM `tasks` WHERE id=:id")
    fun findTaskById(id:Int):Flow<Tasks>

}