package com.example.planer.DAOs

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.planer.entities.Settings

@Dao
interface SettingsDAO {

    @Update
    suspend fun update(settings: Settings)

    @Query("SELECT * FROM Settings WHERE id=1")
    fun getSettings(): LiveData<Settings>

    @Query("SELECT * FROM Settings WHERE id=1")
    fun getSettingsState(): Settings

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(settings: Settings)

    @Delete
    suspend fun delete(settings: Settings)

    @Query("DELETE FROM Settings WHERE id=:id")
    suspend fun deleteById(id: Int)

}