package com.example.planer.DAOs

import androidx.room.*
import com.example.planer.entities.Calendar
import com.example.planer.entities.Tasks
import com.example.planer.entities.relations.TypeAndCalendar
import com.example.planer.entities.relations.TypeAndTasks
import java.sql.Types

@Dao
interface TypeDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(tasks: Tasks)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertType(types: Types)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalendar(calendar: Calendar)

    @Transaction
    @Query("SELECT * FROM Types WHERE id = :id")
    suspend fun getTypeAndTasks(id: Int) : List<TypeAndCalendar>

}