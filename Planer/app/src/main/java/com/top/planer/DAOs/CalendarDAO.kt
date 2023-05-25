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

    @Transaction
    suspend fun insertMapOfCalendarWithNote(map: Map<Calendar,Notes>) {
        map.forEach {
            // if wykrywający powtórki
            if (returnExistingByParameters(it.key.startDate, it.key.endDate, it.key.name).isEmpty()) {
                insertCalendarWithNote(it.key, it.value)
            }
        }
    }

    @Transaction
    suspend fun deleteFromICalList(list: List<Calendar>) {
        list.forEach { event ->
            returnExistingByParameters(event.startDate, event.endDate, event.name).forEach {
                deleteNoteById(it.noteId)
            }
            deleteByParameters(event.startDate, event.endDate, event.name)
        }
    }

    //Delete po parametrach
    @Delete
    suspend fun delete(calendar: Calendar)

    @Query("DELETE FROM Notes WHERE id=:noteId")
    suspend fun deleteNoteById(noteId: Int)

    //Usuwanie wszystkiego
    @Query("DELETE FROM Calendar")
    suspend fun deleteAll()

    //Usuwanie po id
    @Query("DELETE FROM Calendar WHERE id=:id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM Calendar WHERE start_date=:startDate AND end_date=:endDate AND name=:name")
    suspend fun deleteByParameters(startDate: String, endDate: String, name: String)

    @Query("SELECT * FROM Calendar WHERE start_date=:startDate AND end_date=:endDate AND name=:name")
    suspend fun returnExistingByParameters(startDate: String, endDate: String, name: String): List<Calendar>

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