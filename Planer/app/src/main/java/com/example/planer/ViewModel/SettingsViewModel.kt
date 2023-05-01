package com.example.planer.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.planer.AppDatabase
import com.example.planer.entities.ExcludedDate
import com.example.planer.entities.Settings
import com.example.planer.entities.Types
import com.example.planer.repository.ExcludedDateRepository
import com.example.planer.repository.SettingsRepository
import com.example.planer.repository.TypeRepository
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    fun readExcludedDatesFromDb(): LiveData<List<ExcludedDate>> {
        return excludedDateRepository.readExcludedDates()
    }

    suspend fun saveSettings(
        settings: Settings,
        types: List<Types>,
        markedDates: List<ExcludedDate>
    ) {

        typeRepository.updateTypes(types)
        settingsRepository.updateSettings(settings)
        excludedDateRepository.addExcludedDates(markedDates)
        excludedDateRepository.removeDatesNotInList(markedDates)

    }

    suspend fun exportDb(backup: RoomBackup, context: Context): Boolean =
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
                .backup()
            return@withContext deferred.await()
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
}