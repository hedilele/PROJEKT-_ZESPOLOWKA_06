package com.example.planer.repository

import androidx.lifecycle.LiveData
import com.example.planer.DAOs.SettingsDAO
import com.example.planer.entities.Settings

class SettingsRepository(private val settingsDAO: SettingsDAO) {

    fun readSettings(): LiveData<Settings> {
        return settingsDAO.getSettings()
    }

    suspend fun updateSettings(settings: Settings) {
        settingsDAO.update(settings)
    }

    suspend fun setSettings(settings: Settings) {
        settingsDAO.insert(settings)
    }
}