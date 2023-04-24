package com.example.planer.repository

import androidx.lifecycle.LiveData
import com.example.planer.DAOs.NotesDAO
import com.example.planer.entities.Habits
import com.example.planer.entities.Notes

class NoteRepository(private val notesDAO: NotesDAO)
{

    val readAllData: LiveData<List<Notes>> = notesDAO.readAllData()
    val findPomodoro: LiveData<Notes> = notesDAO.findPomoddoroNote()

    suspend fun addNote(notes: Notes)
    {
        notesDAO.insertNote(notes)
    }

    suspend fun updateNote(notes: Notes)
    {
        notesDAO.update(notes)
    }

    suspend fun deleteNote(notes: Notes)
    {
        notesDAO.delete(notes)
    }


    suspend fun readAllDataa()
    {
        notesDAO.readAllData()
    }


    fun getNoteById(id: Int): LiveData<List<Notes>>
    {
        return notesDAO.getNoteById(id)
    }

    fun findPomodoroNote(): LiveData<Notes>
    {
        return notesDAO.findPomoddoroNote()
    }

}