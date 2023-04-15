package com.example.planer.repository

import androidx.lifecycle.LiveData
import com.example.planer.DAOs.NotesDAO
import com.example.planer.entities.Notes
import com.example.planer.entities.relations.NoteAndTask

class NoteRepository(private val notesDAO: NotesDAO)
{
    fun getNoteById(id: Int): LiveData<List<Notes>>
    {
        return notesDAO.getNoteById(id)
    }
}