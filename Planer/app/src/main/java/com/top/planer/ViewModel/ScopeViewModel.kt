package com.top.planer.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.top.planer.AppDatabase
import com.top.planer.algorithm.IO
import com.top.planer.entities.Notes
import com.top.planer.entities.Tasks
import com.top.planer.entities.Types
import com.top.planer.repository.NoteRepository
import com.top.planer.repository.ScopeRepository
import com.top.planer.repository.TypeRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ScopeViewModel(application: Application): AndroidViewModel(application)
{
    private val taskDAO = AppDatabase.getDatabase(application).tasksDAO()
    private val typeDAO = AppDatabase.getDatabase(application).typesDAO()
    private val notesDAO = AppDatabase.getDatabase(application).notesDAO()
    private val scopeRepository: ScopeRepository = ScopeRepository(taskDAO)
    private val typeRepository: TypeRepository = TypeRepository(typeDAO)
    private val noteRepository: NoteRepository = NoteRepository(notesDAO)
    private val unfilteredTasks: LiveData<Map<Tasks, List<Notes>>> = scopeRepository.getOverdueTasks()
    private var mutableTaskMap: MutableLiveData<Map<Tasks, List<Notes>>> = MutableLiveData()

    init {
        mutableTaskMap = MediatorLiveData<Map<Tasks, List<Notes>>>().apply {
            addSource(unfilteredTasks) { value = it }
        }
    }

    fun getScopeTasks(): LiveData<Map<Tasks, List<Notes>>> {
        return mutableTaskMap
    }

    fun getTypes(): LiveData<List<Types>> {
        return typeRepository.getAllTypes()
    }

    suspend fun postponeTaskTomorrow(task: Tasks) {
        val currentDateTime = LocalDateTime.now().plusDays(1)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val formattedDateTime = currentDateTime.format(formatter).toString()

        task.deadline = formattedDateTime
        scopeRepository.updateTask(task)
    }

    suspend fun setTaskAsDone(context: Context, task: Tasks, note: Notes?) {
        val io = IO()
        io.updateWork(context, task.timeToFinish)
        scopeRepository.removeTask(task)
        if (note != null) {
            noteRepository.deleteNote(note)
        }
    }

    fun removeTask(task: Tasks) {
        mutableTaskMap.postValue(mutableTaskMap.value?.filterKeys { it != task } ?: return)
    }
}