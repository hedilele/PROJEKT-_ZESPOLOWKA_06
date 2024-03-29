package com.top.planer.repository

import androidx.lifecycle.LiveData
import com.top.planer.DAOs.SettingsDAO
import com.top.planer.entities.Settings

class SettingsRepository(private val settingsDAO: SettingsDAO) {

    fun readSettings(): LiveData<Settings> {
        return settingsDAO.getSettings()
    }

    fun getSettings(): Settings {
        return settingsDAO.getSettingsState()
    }

    suspend fun updateSettings(settings: Settings) {
        settingsDAO.update(settings)
    }

    suspend fun setSettings(settings: Settings) {
        settingsDAO.insert(settings)
    }

    fun readHours(): LiveData<Int> {
        return settingsDAO.getHours()
    }
}