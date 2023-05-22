package com.top.planer.ViewModel

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.top.planer.AppDatabase
import com.top.planer.entities.ExcludedDate
import com.top.planer.entities.Settings
import com.top.planer.entities.Types
import com.top.planer.gui.pages.settings.UserSettingsActivity
import com.top.planer.repository.ExcludedDateRepository
import com.top.planer.repository.SettingsRepository
import com.top.planer.repository.TypeRepository
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

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

    fun readExcludedDatesFromDb(): LiveData<MutableList<ExcludedDate>> {
        return excludedDateRepository.readExcludedDates()
    }

    suspend fun saveSettings(
        settings: Settings,
        types: List<Types>,
        markedDates: List<ExcludedDate>
    ): Boolean {

        typeRepository.updateTypes(types)
        settingsRepository.updateSettings(settings)
        excludedDateRepository.addExcludedDates(markedDates)
        excludedDateRepository.removeDatesNotInList(markedDates)
        return true

    }

    suspend fun exportDb(backup: RoomBackup, context: Context): Boolean =
        withContext(Dispatchers.IO) {
            val deferred = CompletableDeferred<Boolean>()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val current = LocalDateTime.now().format(formatter)
            backup
                .database(AppDatabase.getDatabase(context))
                .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
                .customBackupFileName("Backup_$current.sqlite3")
                .apply {
                    onCompleteListener { success, message, exitCode ->
                        deferred.complete(success)
                    }
                }
                .backup()
            return@withContext deferred.await()
        }

    fun getHours(): LiveData<Int>{
        return settingsRepository.readHours()
    }

    fun getExcludedDates(): LiveData<MutableList<ExcludedDate>>{
        return excludedDateRepository.readExcludedDates()
    }

    suspend fun importDb(backup: RoomBackup, context: Context): Boolean =
        withContext(Dispatchers.IO) {
            val deferred = CompletableDeferred<Boolean>()
            backup
                .database(AppDatabase.getDatabase(context))
                .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
                .apply {
                    onCompleteListener { success, message, exitCode ->
                        deferred.complete(success)
                    }
                }
                .restore()
            return@withContext deferred.await()
        }

    fun restartApp(backup: RoomBackup, context: Context) {
        backup.restartApp(Intent(context, UserSettingsActivity::class.java))
    }
}