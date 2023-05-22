package com.top.planer.DAOs

import androidx.lifecycle.LiveData
import androidx.room.*
import com.top.planer.entities.Types

@Dao
interface TypeDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertType(types: Types)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTypes(types: List<Types>)

    @Update
    suspend fun updateType(type: Types)

    @Update
    suspend fun updateTypes(types: List<Types>)

    @Transaction
    @Query("SELECT * FROM Types")
    fun getAllTypes() : LiveData<List<Types>>

    @Query("SELECT * FROM Types")
    fun getAllTypesList() : List<Types>

}