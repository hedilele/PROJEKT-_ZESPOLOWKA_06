package com.example.planer.DAOs

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.planer.entities.LastAccess

@Dao
interface LastAccessDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lastAccess: LastAccess)

    @Update
    suspend fun update(lastAccess: LastAccess)

    @Query("SELECT * FROM LastAccess WHERE id=1")
    fun getLastAccess(): LiveData<LastAccess>
}