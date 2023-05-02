package com.example.planer.ViewModel

import android.app.Application
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.planer.AppDatabase
import com.example.planer.entities.Habits
import com.example.planer.entities.Notes
import com.example.planer.repository.HabitRepository
import com.example.planer.repository.NoteRepository
import kotlinx.coroutines.*

//Referencja do aplikacji
class NoteViewModel(application: Application): AndroidViewModel(application)
{
    val readAllData: LiveData<List<Notes>>
    val readAllShortNotes: LiveData<List<Notes>>
    private val repository: NoteRepository

    //To zawsze pierwsze bedzie sie wykonywalo kiedy callujemy UserViewModel
    init
    {
        val notesDAO = AppDatabase.getDatabase(application).notesDAO()
        repository = NoteRepository(notesDAO)
        readAllData = repository.readAllData
        readAllShortNotes = repository.readAllShortNotes
    }

    //Zla praktyka jest uruchamiac zapytania z bazy w watku glownym!
    fun addNote(notes: Notes)
    {
        viewModelScope.launch(Dispatchers.IO) //Odpali sie w oddzielnym watku w tle
        {
            repository.addNote(notes)
        }
    }

    fun deleteNote(notes: Notes)
    {
        viewModelScope.launch(Dispatchers.IO) //Odpali sie w oddzielnym watku w tle
        {
            repository.deleteNote(notes)
        }
    }

    fun deleteNoteById(id: Int)
    {
        viewModelScope.launch(Dispatchers.IO) //Odpali sie w oddzielnym watku w tle
        {
            repository.deleteNoteById(id)
        }
    }

    fun getNoteById(id: Int): Notes
    {
        return repository.getNoteById(id)

    }

    fun updateNote(notes: Notes)
    {
        viewModelScope.launch(Dispatchers.IO) //Odpali sie w oddzielnym watku w tle
        {
            repository.updateNote(notes)
        }
    }

    fun updateNoteById(id: Int, context: String)
    {
        viewModelScope.launch(Dispatchers.IO) //Odpali sie w oddzielnym watku w tle
        {
            repository.updateNoteById(id, context)
        }
    }

    fun readAllData()
    {
        viewModelScope.launch (Dispatchers.IO )
        {
            repository.readAllDataa()
        }
    }

}