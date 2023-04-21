package com.example.planer.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.planer.AppDatabase
import com.example.planer.entities.Settings
import com.example.planer.repository.SettingsRepository

class SettingsViewModel(application: Application): AndroidViewModel(application) {

    private val settingsDAO = AppDatabase.getDatabase((application)).settingsDAO()
    private val settingsRepository = SettingsRepository(settingsDAO)

    fun readSettingsFromDb(): LiveData<Settings> {
        return settingsRepository.readSettings()
    }

    suspend fun saveSettings(settings: Settings) {
        settingsRepository.updateSettings(settings)
    }

    suspend fun createSettingsIfDontExist(settings: Settings){
        settingsRepository.setSettings(settings)
    }

}