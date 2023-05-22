package com.top.planer.DAOs

import androidx.lifecycle.LiveData
import androidx.room.*
import com.top.planer.entities.Calendar
import com.top.planer.entities.Notes

@Dao
interface CalendarDAO
{
    //Insertowanie do kalendarza wszystkich rzeczy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(calendar: Calendar)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Notes): Long

    @Transaction
    suspend fun insertCalendarWithNote(calendar: Calendar, note: Notes) {
        val noteId = insertNote(note)
        calendar.noteId = noteId.toInt()
        insert(calendar)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalendar(calendar: Calendar): Long

    @Transaction
    suspend fun insertCalendarWithNoteAndGetId(calendar: Calendar, note: Notes): Long {
        val noteId = insertNote(note)
        calendar.noteId = noteId.toInt()
        return insertCalendar(calendar)
    }

    //Delete po parametrach
    @Delete
    suspend fun delete(calendar: Calendar)

    //Usuwanie wszystkiego
    @Query("DELETE FROM Calendar")
    suspend fun deleteAll()

    //Usuwanie po id
    @Query("DELETE FROM Calendar WHERE id=:id")
    suspend fun deleteById(id: Long)

    //Update
    @Update
    suspend fun update(calendar: Calendar)

    //Zwraca rekordy z kalendarza po id
    @Query("SELECT * FROM Calendar WHERE id = :id")
    suspend fun getCalendarDateById(id: Long): Calendar

    //Zwraca wszystkie daty
    @Query("SELECT * FROM Calendar")
    fun getAllDates():LiveData<List<Calendar>>

    //Zwraca wszystkie daty do listy do exportu
    @Query("SELECT * FROM Calendar")
    fun getAllDatesList(): List<Calendar>

    //Podaje date w string i wypluwa wydarzenia występujące podczas tej daty
    // query_date musi być date (yyyy-mm-dd) a nie datetime
    @Query("SELECT * FROM Calendar WHERE :query_date BETWEEN date(start_date) AND date(end_date)")
    fun getCalendarsFromDate(query_date: String): LiveData<List<Calendar>>

}