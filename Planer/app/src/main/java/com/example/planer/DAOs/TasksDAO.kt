package com.example.planer.DAOs

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.planer.entities.Finished
import com.example.planer.entities.Subtasks
import com.example.planer.entities.Tasks
import com.example.planer.entities.relations.TaskAndFinished
import com.example.planer.entities.relations.TaskAndSubtasks
import com.example.planer.entities.relations.TypeAndTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDAO
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tasks: Tasks)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFinished(finished: Finished)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSubtask(subtasks: Subtasks)

    @Update
    suspend fun update(tasks: Tasks)

    @Delete
    suspend fun delete(tasks: Tasks)

    @Query("SELECT * FROM `tasks`")
    fun fetchAll(): Flow<List<Tasks>>

    @Query("SELECT * FROM Tasks ORDER BY id ASC")
    fun readAllData(): LiveData<List<Tasks>>

    @Query("SELECT * FROM `tasks` WHERE id=:id")
    fun findTaskById(id:Int):Flow<Tasks>

    @Transaction
    @Query("SELECT * FROM Tasks WHERE id = :id")
    suspend fun getTaskAndFinished(id: Int) : List<TaskAndFinished>

    @Transaction
    @Query("SELECT * FROM Tasks WHERE id = :id")
    suspend fun getTaskAndSubtasks(id: Int) : List<TaskAndSubtasks>

}