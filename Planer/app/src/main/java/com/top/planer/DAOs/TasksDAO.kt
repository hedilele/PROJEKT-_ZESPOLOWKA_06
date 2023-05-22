package com.top.planer.DAOs

import androidx.lifecycle.LiveData
import androidx.room.*
import com.top.planer.entities.Finished
import com.top.planer.entities.Notes
import com.top.planer.entities.Subtasks
import com.top.planer.entities.Tasks
import com.top.planer.entities.relations.TaskAndFinished
import com.top.planer.entities.relations.TaskAndSubtasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDAO
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tasks: Tasks)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: Notes): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFinished(finished: Finished)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSubtask(subtasks: Subtasks)

    @Transaction
    suspend fun insertTaskWithNote(task: Tasks, note: Notes) {
        val noteId = insertNote(note)
        task.noteId = noteId.toInt()
        insert(task)
    }

    @Update
    suspend fun update(tasks: Tasks)

    @Delete
    suspend fun delete(tasks: Tasks)

    //usuwanie taska po id
    @Query("DELETE FROM Tasks WHERE id=:id")
    suspend fun deleteById(id: Int)
    //@Query("SELECT * FROM Tasks WHERE strftime('%d-%m-%Y', deadline) = strftime('%d-%m-%Y', 'now')")
    @Query("SELECT * FROM Tasks WHERE deadline = '31-03-2023'")
    suspend fun getCurrentDays(): List<Tasks>

    @Query("SELECT * FROM Tasks")
    fun getTasksList(): List<Tasks>

    @Query("SELECT * FROM `tasks`")
    fun fetchAll(): Flow<List<Tasks>>

    @Query("SELECT * FROM Tasks ORDER BY id ASC")
    fun readAllData(): LiveData<List<Tasks>>

    @Query("SELECT * FROM Tasks")
    fun readAllDataa(): LiveData<List<Tasks>>

    @Query("SELECT * FROM Tasks ORDER BY title DESC")
    fun readAllDataWithFilter(): List<Tasks>

    @Query("SELECT * FROM `tasks` WHERE id=:id")
    fun findTaskById(id:Int): List<Tasks>

    @Query("SELECT datetime(deadline) FROM Tasks")
    fun readAllDeadlines(): List<String> //Zeby zwracac pojedyncze kolumny to musimy uzyc typu prostego

    //Wyszukiwanie po nazwie
    @Query("SELECT * FROM Tasks WHERE title LIKE '%' || :searchName || '%'")
    fun readTasksWithSearchName(searchName: String) : LiveData<List<Tasks>>

    //Czytanie po typach
    @Query("SELECT * FROM Tasks WHERE type_id IN (:typeIds)")
    fun readTasksWithTypes(typeIds: List<Int>): LiveData<List<Tasks>>

    //Czytanie po czasie trwania
    @Query("SELECT * FROM Tasks WHERE time_to_finish IN (:timeToFinishes)")
    fun readTasksWithDuration(timeToFinishes: List<Int>): LiveData<List<Tasks>>

    //Czytanie po dacie
    @Query("SELECT * FROM Tasks WHERE deadline BETWEEN :startDate AND :endDate")
    fun readTasksWithTime(startDate: String, endDate: String): LiveData<List<Tasks>>

    //Czytanie po czasie trwania i po typie
    @Query("SELECT * FROM Tasks WHERE type_id IN (:typeIds) AND time_to_finish IN (:timeToFinishes)")
    fun readTasksWithTypesAndDurations(typeIds: List<Int>, timeToFinishes: List<Int>): LiveData<List<Tasks>>

    //Czytanie po czasie trwania i dacie
    @Query("SELECT * FROM Tasks WHERE time_to_finish IN (:timeToFinishes) AND deadline BETWEEN :startDate AND :endDate")
    fun readTasksWithDurationAndTime(timeToFinishes: List<Int>,startDate: String, endDate: String): LiveData<List<Tasks>>

    //Czytanie po typie i dacie
    @Query("SELECT * FROM Tasks WHERE type_id IN (:typeIds) AND deadline BETWEEN :startDate AND :endDate")
    fun readTasksWithTypesAndTime(typeIds: List<Int>, startDate: String, endDate: String): LiveData<List<Tasks>>

    //Czytanie po wszystkich parametrach
    @Query("SELECT * FROM Tasks WHERE type_id IN (:typeIds) AND time_to_finish IN (:timeToFinishes) AND deadline BETWEEN :startDate AND :endDate")
    fun readTasksWithTypesAndTimeAndDuration(typeIds: List<Int>, timeToFinishes: List<Int>,startDate: String, endDate: String): LiveData<List<Tasks>>

    // Pobiera łączone typy Tasks i Notes do przekazania scope mode aka są aktywne, deadline minął lub są nieważne niepilne
    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM Tasks t " +
        "LEFT JOIN Notes n ON t.note_id = n.id " +
        "WHERE t.is_active = 1 " +
        "AND (strftime('%s', t.deadline) < strftime('%s', 'now') OR (t.importance = 0 AND t.urgency = 0))" +
            "ORDER BY t.deadline DESC")
    fun readOverdueTasksWithNotes(): LiveData<Map<Tasks, List<Notes>>>

    @Transaction
    @Query("SELECT * FROM Tasks WHERE id = :id")
    suspend fun getTaskAndFinished(id: Int) : List<TaskAndFinished>

    @Transaction
    @Query("SELECT * FROM Tasks WHERE id = :id")
    suspend fun getTaskAndSubtasks(id: Int) : List<TaskAndSubtasks>

}