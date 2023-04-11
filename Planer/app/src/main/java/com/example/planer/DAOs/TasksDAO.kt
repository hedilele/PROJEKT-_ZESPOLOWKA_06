package com.example.planer.DAOs

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.planer.entities.Finished
import com.example.planer.entities.Subtasks
import com.example.planer.entities.Tasks
import com.example.planer.entities.relations.NoteAndTask
import com.example.planer.entities.relations.TaskAndFinished
import com.example.planer.entities.relations.TaskAndSubtasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDAO
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tasks: Tasks)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFinished(finished: Finished)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSubtask(subtasks: Subtasks)

    @Update
    suspend fun update(tasks: Tasks)

    @Delete
    suspend fun delete(tasks: Tasks)

    //usuwanie taska po id
    @Query("DELETE FROM Tasks WHERE id=:id")
    suspend fun deleteById(id: Long)

    //@Query("SELECT * FROM Tasks WHERE strftime('%d-%m-%Y', deadline) = strftime('%d-%m-%Y', 'now')")
    @Query("SELECT * FROM Tasks WHERE deadline = '31-03-2023'")
    suspend fun getCurrentDays(): List<Tasks>

    @Query("SELECT * FROM `tasks`")
    fun fetchAll(): Flow<List<Tasks>>

    @Query("SELECT * FROM Tasks ORDER BY id ASC")
    fun readAllData(): LiveData<List<Tasks>>

    @Query("SELECT * FROM Tasks ORDER BY title DESC")
    fun readAllDataWithFilter(): List<Tasks>

    @Query("SELECT * FROM `tasks` WHERE id=:id")
    fun findTaskById(id:Int): List<Tasks>

    @Query("SELECT datetime(deadline) FROM Tasks")
    fun readAllDeadlines(): List<String> //Zeby zwracac pojedyncze kolumny to musimy uzyc typu prostego

    //Musze stworzyc zapytania do bazy, ktore beda elastyczne pod wzgledem wyboru i uniwersalne
    //Przykladowo tutaj dla deadline
    //PROBLEM - Jak bedzie wygladalo dokladnie takie zapytanie
    //UPDATE - takie zapytanie zwraca nam te taski z przedzialem
    //Mozna to ograc takimi zapytaniami SELECT * FROM Tasks WHERE type_id IN (3,1) AND time_to_finish IN(1)
    //PROBLEM - Jak w zasadzie polaczyc te typy,
    @Query("SELECT * FROM Tasks WHERE type_id IN (:typeId)") //Czysto przykladowo
    fun readTasksWithDeadline(typeId: List<Int>): List<Tasks>

    // Pobiera łączone typy Tasks i Notes do przekazania scope mode aka deadline minął
    @Transaction
    @Query("SELECT * FROM Tasks t JOIN Notes n ON t.note_id=n.id WHERE strftime('%s', t.deadline) < strftime('%s', 'now')")
    fun readOverdueTasksWithNotes(): LiveData<List<NoteAndTask>>

    @Transaction
    @Query("SELECT * FROM Tasks WHERE id = :id")
    suspend fun getTaskAndFinished(id: Int) : List<TaskAndFinished>

    @Transaction
    @Query("SELECT * FROM Tasks WHERE id = :id")
    suspend fun getTaskAndSubtasks(id: Int) : List<TaskAndSubtasks>

}