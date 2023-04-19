package com.example.planer.DAOs

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.planer.entities.Settings

@Dao
interface SettingsDAO {

    @Update
    suspend fun update(settings: Settings)

    @Query("SELECT * FROM Settings WHERE id=0")
    fun getSettings(): LiveData<Settings>

    // Poniżej teoretycznie niepotrzebne ale w razie czego są.

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(settings: Settings)

    @Delete
    suspend fun delete(settings: Settings)

    @Query("DELETE FROM Settings WHERE id=:id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM Settings")
    suspend fun deleteAll()

}