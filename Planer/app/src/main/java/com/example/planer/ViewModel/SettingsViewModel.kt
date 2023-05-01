package com.example.planer.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.planer.AppDatabase
import com.example.planer.entities.ExcludedDate
import com.example.planer.entities.Settings
import com.example.planer.entities.Types
import com.example.planer.gui.pages.settings.DbExport
import com.example.planer.repository.CalendarRepository
import com.example.planer.repository.ExcludedDateRepository
import com.example.planer.repository.HabitRepository
import com.example.planer.repository.NoteRepository
import com.example.planer.repository.SettingsRepository
import com.example.planer.repository.TaskRepository
import com.example.planer.repository.TypeRepository
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class SettingsViewModel(application: Application): AndroidViewModel(application) {

    private val settingsDAO = AppDatabase.getDatabase((application)).settingsDAO()
    private val typesDAO = AppDatabase.getDatabase((application)).typesDAO()
    private val excludedDateDAO = AppDatabase.getDatabase((application)).excludedDateDAO()
    private val calendarDAO = AppDatabase.getDatabase((application)).calendarDAO()
    private val habitsDAO = AppDatabase.getDatabase((application)).habitsDAO()
    private val notesDAO = AppDatabase.getDatabase((application)).notesDAO()
    private val tasksDAO = AppDatabase.getDatabase((application)).tasksDAO()

    private val settingsRepository = SettingsRepository(settingsDAO)
    private val typeRepository = TypeRepository(typesDAO)
    private val excludedDateRepository = ExcludedDateRepository(excludedDateDAO)
    private val calendarRepository = CalendarRepository(calendarDAO)
    private val habitsRepository = HabitRepository(habitsDAO)
    private val notesRepository = NoteRepository(notesDAO)
    private val taskRepository = TaskRepository(tasksDAO)

    fun readSettingsFromDb(): LiveData<Settings> {
        return settingsRepository.readSettings()
    }

    fun readTypesFromDb(): LiveData<List<Types>> {
        return typeRepository.getAllTypes()
    }

    fun readExcludedDatesFromDb(): LiveData<List<ExcludedDate>> {
        return excludedDateRepository.readExcludedDates()
    }

    suspend fun saveSettings(settings: Settings, types: List<Types>, markedDates: List<ExcludedDate>) {

        typeRepository.updateTypes(types)
        settingsRepository.updateSettings(settings)
        excludedDateRepository.addExcludedDates(markedDates)
        excludedDateRepository.removeDatesNotInList(markedDates)

    }

    suspend fun createSettingsIfDontExist() {
        settingsRepository.setSettings(Settings()) // domyślny konstruktor
    }

    suspend fun createTypesIfDontExist() {
        typeRepository.addType(Types(1, "Dom", "#81A969"))
        typeRepository.addType(Types(2, "Szkoła", "#88A9C3"))
        typeRepository.addType(Types(3, "Komputer", "#FFEB5B"))
        typeRepository.addType(Types(4, "Rozrywka", "#FFC0CB"))
    }

    fun exportDb(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val dataList = DbExport(
                calendar = calendarRepository.getAllList(),
                excludedDate = excludedDateRepository.getExcludedDatesList(),
                habits = habitsRepository.getHabitsList(),
                notes = notesRepository.getAllNotesList(),
                settings = settingsRepository.getSettings(),
                tasks = taskRepository.getTasksList(),
                types = typeRepository.getAllTypesList()
            )
            val gson = Gson()
            val jsonString = gson.toJson(dataList)
            try {
                val file = File(context.filesDir,"dbexport.json")
                file.writeText(jsonString)
                // return success
            } catch (e: Exception) {
                // return failed
            }
        }
    }

}