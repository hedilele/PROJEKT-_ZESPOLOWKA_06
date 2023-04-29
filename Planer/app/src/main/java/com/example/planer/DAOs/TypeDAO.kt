package com.example.planer.DAOs

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.planer.entities.Types

@Dao
interface TypeDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertType(types: Types)

    @Update
    suspend fun updateType(type: Types)

    @Update
    suspend fun updateTypes(types: List<Types>)

    @Transaction
    @Query("SELECT * FROM Types")
    fun getAllTypes() : LiveData<List<Types>>

}