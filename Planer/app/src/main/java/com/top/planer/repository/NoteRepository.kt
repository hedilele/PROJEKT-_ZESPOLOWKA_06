package com.top.planer.repository

import androidx.lifecycle.LiveData
import com.top.planer.DAOs.NotesDAO
import com.top.planer.entities.Notes

class NoteRepository(private val notesDAO: NotesDAO)
{

    val readAllData: LiveData<List<Notes>> = notesDAO.readAllData()
    val readAllShortNotes: LiveData<List<Notes>> = notesDAO.readAllShortNotes()

    suspend fun addNote(notes: Notes)
    {
        notesDAO.insertNote(notes)
    }

    suspend fun updateNote(notes: Notes)
    {
        notesDAO.update(notes)
    }

    suspend fun updateNoteById(id: Int, context: String)
    {
        notesDAO.updateNoteById(id, context)
    }

    suspend fun deleteNote(notes: Notes)
    {
        notesDAO.delete(notes)
    }

    suspend fun deleteNoteById(id: Int)
    {
        notesDAO.deleteByID(id)
    }

    suspend fun readAllDataa()
    {
        notesDAO.readAllData()
    }

    fun getNoteById(id: Int): Notes
    {
        return notesDAO.getNoteById(id)
    }

    fun getAllNotesList(): List<Notes> {
        return notesDAO.getAllNotesList()
    }

}