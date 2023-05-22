package com.top.planer.DAOs

import androidx.lifecycle.LiveData
import androidx.room.*
import com.top.planer.entities.ExcludedDate
import java.time.LocalDate

@Dao
interface ExcludedDateDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(excludedDate: ExcludedDate)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDates(excludedDates: List<ExcludedDate>)

    @Update
    suspend fun update(excludedDate: ExcludedDate)

    @Query("SELECT * FROM ExcludedDate")
    fun getExcludedDates(): LiveData<MutableList<ExcludedDate>>

    @Query("SELECT * FROM ExcludedDate")
    fun getExcludedDatesList(): List<ExcludedDate>

    @Query("SELECT * FROM ExcludedDate WHERE id=:id")
    fun getExcludedDateById(id: Int): LiveData<ExcludedDate>

    @Delete
    suspend fun delete(excludedDate: ExcludedDate)

    @Query("DELETE FROM ExcludedDate WHERE date=:date")
    suspend fun deleteByDate(date: LocalDate)

    @Query("DELETE FROM ExcludedDate WHERE date NOT IN (:dates)")
    suspend fun deleteDatesNotInList(dates: List<LocalDate>)

    @Query("DELETE FROM ExcludedDate WHERE id=:id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM ExcludedDate")
    suspend fun deleteAll()

}