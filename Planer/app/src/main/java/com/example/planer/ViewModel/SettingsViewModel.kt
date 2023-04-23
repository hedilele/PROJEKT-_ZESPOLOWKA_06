package com.example.planer.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.planer.AppDatabase
import com.example.planer.entities.Settings
import com.example.planer.entities.Types
import com.example.planer.repository.SettingsRepository
import com.example.planer.repository.TypeRepository

class SettingsViewModel(application: Application): AndroidViewModel(application) {

    private val settingsDAO = AppDatabase.getDatabase((application)).settingsDAO()
    private val typesDAO = AppDatabase.getDatabase((application)).typesDAO()

    private val settingsRepository = SettingsRepository(settingsDAO)
    private val typeRepository = TypeRepository(typesDAO)

    fun readSettingsFromDb(): LiveData<Settings> {
        return settingsRepository.readSettings()
    }

    fun readTypesFromDb(): LiveData<List<Types>> {
        return typeRepository.getAllTypes()
    }

    suspend fun saveSettings(settings: Settings) {
        settingsRepository.updateSettings(settings)
    }

    suspend fun createSettingsIfDontExist(settings: Settings){
        settingsRepository.setSettings(settings)
    }

}