package com.top.planer.DAOs

import androidx.room.*
import com.top.planer.entities.Calendar
import com.top.planer.entities.Repeat
import com.top.planer.entities.relations.RepeatAndCalendar

@Dao
interface RepeatDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepeat(repeat: Repeat)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalendar(calendar: Calendar)

    @Transaction
    @Query("SELECT * FROM Repeat WHERE id = :id")
    suspend fun getRepeatAndCalendar(id: Int) : List<RepeatAndCalendar>
}