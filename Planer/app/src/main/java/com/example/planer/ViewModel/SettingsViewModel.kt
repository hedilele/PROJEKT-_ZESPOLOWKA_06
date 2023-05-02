package com.example.planer.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.planer.AppDatabase
import com.example.planer.entities.ExcludedDate
import com.example.planer.entities.Settings
import com.example.planer.entities.Types
import com.example.planer.repository.ExcludedDateRepository
import com.example.planer.repository.SettingsRepository
import com.example.planer.repository.TypeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsViewModel(application: Application): AndroidViewModel(application) {

    private val settingsDAO = AppDatabase.getDatabase((application)).settingsDAO()
    private val typesDAO = AppDatabase.getDatabase((application)).typesDAO()
    private val excludedDateDAO = AppDatabase.getDatabase((application)).excludedDateDAO()

    private val settingsRepository = SettingsRepository(settingsDAO)
    private val typeRepository = TypeRepository(typesDAO)
    private val excludedDateRepository = ExcludedDateRepository(excludedDateDAO)

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

    fun getHours(): LiveData<Int>{
        return settingsRepository.readHours()
    }

}