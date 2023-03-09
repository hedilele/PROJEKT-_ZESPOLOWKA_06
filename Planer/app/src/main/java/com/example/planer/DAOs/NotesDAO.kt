package com.example.planer.DAOs

import androidx.room.*
import com.example.planer.entities.Calendar
import com.example.planer.entities.Notes
import com.example.planer.entities.Tasks
import com.example.planer.entities.relations.NoteAndCalendar
import com.example.planer.entities.relations.NoteAndTask

@Dao
interface NotesDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(notes: Notes)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(tasks: Tasks)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalendar(calendar: Calendar)

    @Transaction
    @Query("SELECT * FROM Note WHERE id = :id")
    suspend fun getNoteAndTaskWithId(id: Int) : List<NoteAndTask>

    @Transaction
    @Query("SELECT * FROM Note WHERE id = :id")
    suspend fun getNoteAndCalendar(id: Int) : List<NoteAndCalendar>
}