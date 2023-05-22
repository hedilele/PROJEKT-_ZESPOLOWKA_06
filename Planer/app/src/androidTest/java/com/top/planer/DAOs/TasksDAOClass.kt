package com.top.planer.DAOs

import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.top.planer.AppDatabase
import com.top.planer.entities.Tasks
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TasksDAOClass {

    @get:Rule

    private lateinit var database: AppDatabase
    private lateinit var dao: TasksDAO


    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.tasksDAO()

    }

    @After
    fun teardown(){
        database.close()
    }



    //jeżeli test przechodzi, to znaczy, że task jest poprawnie dodawany i odczytywany z bazy
    @Test
    fun insertTaskTest() = runTest{

        val task = Tasks(1, "common_task", 1, 1, "27-03-2023",  1, 1, 1, 1)

        dao.insert(task)

        val result = dao.fetchAll().first()
        assertEquals(1, result.size)
        assertEquals(task, result[0])
    }

    //przechodzi, jeśli nie da się insertować dwa razy tego samego taska
    @Test
    fun insertSameTaskTwice() = runTest {
        val task = Tasks(1, "common_task", 1, 1, "27-03-2023",  1, 1, 1, 1)

        dao.insert(task)
        dao.insert(task)


        val result = dao.fetchAll().first()
        assertEquals(1, result.size)
        assertEquals(task, result[0])
    }

    //Odczytywanie deadlinow z taskow
    @Test
    fun checkDeadlines() = runTest{
        val task1 = Tasks(1, "common_task", 2, 2, "27-03-2023",  1, 1, 1, 1)
        val task2 = Tasks(2, "common_task2", 2, 2, "30-03-2023",  1, 1, 1, 1)

        dao.insert(task1)
        dao.insert(task2)

        val result = dao.readAllDeadlines()
        var lista = listOf("27-03-2023","30-03-2023")
        assertThat(result).isEqualTo(lista)
    }

    /*
    @Test
    fun dateConversion() = runTest{
        val task = Tasks(1,"task132",2,2,"10-04-2023",1,1,1,1)

        dao.insert(task)


        val
    }
     */


    //przechodzi, jeśli updatowanie Tasków działa
    @Test
    fun updateTask() = runTest {
        val task = Tasks(1, "string", 0, 0, "24-03-2023", 1, 1, 1, 1)

        dao.insert(task)

        val task2 = Tasks(1, "real name", 1, 1, "30-03-2023", 0, 0, 0, 0)

        dao.update(task2)

        val task3 = Tasks(1, "fake name", 0, 1, "29-03-2023", 0, 0, 0, 0)

        dao.update(task3)

        val task4 = Tasks(1, "true task", 1, 0, "27-03-2023", 0, 0, 0, 0)

        dao.update(task4)

        val result = dao.fetchAll().first()

        assertEquals(1, result.size)
        assertEquals(task4, result[0])
    }


    //przechodzi, jeśli update się nie dokonał
    @Test
    fun updateNotExistingTask() = runTest {
        val task = Tasks(1, "string", 0, 0, "24-03-2023", 1, 1, 1, 1)

        dao.insert(task)

        val task4 = Tasks(2, "true task", 1, 0, "27-03-2023", 0, 0, 0, 0)

        dao.update(task4)

        val result = dao.fetchAll().first()

        assertEquals(1, result.size)
        assertEquals(task, result[0])
    }

    //przechodzi, jeśli usuwanie tasków działa
    @Test
    fun deleteTest() = runTest {

        val task = Tasks(1, "string", 0, 0, "24-03-2023", 1, 1, 1, 1)

        dao.insert(task)

        dao.delete(task)

        val result = dao.fetchAll().first()

        assertEquals(0, result.size)
    }


    //sprawdzenie, czy rzeczywiście delete usuwa taska, który został podany w argumencie funkcji...
    //...czy tylko patrzy na ID
    //nie przechodzi, jeśli delete usuwa po samym ID (mimo podawania całego Taska)
    //TODO nie przechodzi
    @Test
    fun deleteByID() = runTest {
        val task = Tasks(1, "string", 0, 0, "24-03-2023", 1, 1, 1, 1)

        dao.insert(task)

        val taskD = Tasks(1, "integer", 1, 1, "23-02-2022", 0, 0, 0, 0)

        //Tak jak w Habits, dodalem do Dao nowy Queries do usuwania po samym id
        dao.deleteById(1)

        val result = dao.fetchAll().first()

        assertEquals(1, result.size)
        assertEquals(task, result[0])
    }

    //sprawdzenie, czy rzeczywiście delete usuwa taska, który został podany w argumencie funkcji...
    //...czy tylko patrzy na Title
    //nie przechodzi, jeśli delete usuwa po samym Title (mimo podawania całego Taska)
    @Test
    fun deleteByTitle() = runTest {
        val task = Tasks(1, "string", 0, 0, "24-03-2023", 1, 1, 1, 1)

        dao.insert(task)

        val taskD = Tasks(2, "string", 1, 1, "23-02-2022", 0, 0, 0, 0)

        dao.delete(taskD)

        val result = dao.fetchAll().first()

        assertEquals(1, result.size)
        assertEquals(task, result[0])
    }

    //sprawdzenie, czy rzeczywiście delete usuwa taska, który został podany w argumencie funkcji...
    //...czy tylko patrzy na Importance
    //nie przechodzi, jeśli delete usuwa po samym Importance (mimo podawania całego Taska)
    @Test
    fun deleteByImportance() = runTest {
        val task = Tasks(1, "string", 0, 0, "24-03-2023", 1, 1, 1, 1)

        dao.insert(task)

        val taskD = Tasks(2, "integer", 0, 1, "23-02-2022", 0, 0, 0, 0)

        dao.delete(taskD)

        val result = dao.fetchAll().first()

        assertEquals(1, result.size)
        assertEquals(task, result[0])
    }

    //sprawdzenie, czy rzeczywiście delete usuwa taska, który został podany w argumencie funkcji...
    //...czy tylko patrzy na Urgency
    //nie przechodzi, jeśli delete usuwa po samym Urgency (mimo podawania całego Taska)
    @Test
    fun deleteByUrgency() = runTest {
        val task = Tasks(1, "string", 0, 0, "24-03-2023", 1, 1, 1, 1)

        dao.insert(task)

        val taskD = Tasks(2, "integer", 1, 0, "23-02-2022", 0, 0, 0, 0)

        dao.delete(taskD)

        val result = dao.fetchAll().first()

        assertEquals(1, result.size)
        assertEquals(task, result[0])
    }

    //sprawdzenie, czy rzeczywiście delete usuwa taska, który został podany w argumencie funkcji...
    //...czy tylko patrzy na Deadline
    //nie przechodzi, jeśli delete usuwa po samym Deadline (mimo podawania całego Taska)
    @Test
    fun deleteByDeadline() = runTest {
        val task = Tasks(1, "string", 0, 0, "24-03-2023", 1, 1, 1, 1)

        dao.insert(task)

        val taskD = Tasks(2, "integer", 1, 1, "24-03-2023", 0, 0, 0, 0)

        dao.delete(taskD)

        val result = dao.fetchAll().first()

        assertEquals(1, result.size)
        assertEquals(task, result[0])
    }

    //sprawdzenie, czy rzeczywiście delete usuwa taska, który został podany w argumencie funkcji...
    //...czy tylko patrzy na TimeToFinish
    //nie przechodzi, jeśli delete usuwa po samym TimeToFinish (mimo podawania całego Taska)
    @Test
    fun deleteByTimeToFinish() = runTest {
        val task = Tasks(1, "string", 0, 0, "24-03-2023", 1, 1, 1, 1)

        dao.insert(task)

        val taskD = Tasks(2, "dude", 1, 1, "23-02-2022", 1, 0, 0, 0)

        dao.delete(taskD)

        val result = dao.fetchAll().first()

        assertEquals(1, result.size)
        assertEquals(task, result[0])
    }

    //sprawdzenie, czy rzeczywiście delete usuwa taska, który został podany w argumencie funkcji...
    //...czy tylko patrzy na IsActive
    //nie przechodzi, jeśli delete usuwa po samym IsActive (mimo podawania całego Taska)
    @Test
    fun deleteByIsActive() = runTest {
        val task = Tasks(1, "string", 0, 0, "24-03-2023", 1, 1, 1, 1)

        dao.insert(task)

        val taskD = Tasks(2, "integer", 1, 1, "23-02-2022", 0, 1, 0, 0)

        dao.delete(taskD)

        val result = dao.fetchAll().first()

        assertEquals(1, result.size)
        assertEquals(task, result[0])
    }

    //sprawdzenie, czy rzeczywiście delete usuwa taska, który został podany w argumencie funkcji...
    //...czy tylko patrzy na TypeID
    //nie przechodzi, jeśli delete usuwa po samym TypeID (mimo podawania całego Taska)
    @Test
    fun deleteByTypeId() = runTest {
        val task = Tasks(1, "string", 0, 0, "24-03-2023", 1, 1, 1, 1)

        dao.insert(task)

        val taskD = Tasks(2, "title", 1, 1, "23-02-2022", 0, 0, 1, 0)

        dao.delete(taskD)

        val result = dao.fetchAll().first()

        assertEquals(1, result.size)
        assertEquals(task, result[0])
    }

    //sprawdzenie, czy rzeczywiście delete usuwa taska, który został podany w argumencie funkcji...
    //...czy tylko patrzy na NoteID
    //nie przechodzi, jeśli delete usuwa po samym NoteID (mimo podawania całego Taska)
    @Test
    fun deleteByNoteId() = runTest {
        val task = Tasks(1, "string", 0, 0, "24-03-2023", 1, 1, 1, 1)

        dao.insert(task)

        val taskD = Tasks(2, "title", 1, 1, "23-02-2022", 0, 0, 0, 1)

        dao.delete(taskD)

        val result = dao.fetchAll().first()

        assertEquals(1, result.size)
        assertEquals(task, result[0])
    }


    //sprawdzenie czy działa odczytywanie wielu danych przy pomocy fetchAll()
    @Test
    fun fetchAllTest() = runTest {
        val task = Tasks(1, "string", 0, 0, "24-03-2023", 1, 1, 1, 1)
        val task1 = Tasks(2, "string2", 0, 0, "24-03-2023", 1, 1, 1, 1)
        val task2 = Tasks(3, "string3", 0, 0, "24-03-2023", 1, 1, 1, 1)

        dao.insert(task)
        dao.insert(task1)
        dao.insert(task2)

        val result = dao.fetchAll().first()

        assertEquals(3, result.size)
        assertEquals(task, result[0])
        assertEquals(task1, result[1])
        assertEquals(task2, result[2])
    }

    //sprawdzenie czy działa odczytywanie wielu danych przy pomocy readAllData()
    @Test
    fun readAllDataTest() = runTest {
        val task = Tasks(1, "string", 0, 0, "24-03-2023", 1, 1, 1, 1)
        val task1 = Tasks(2, "string2", 0, 0, "24-03-2023", 1, 1, 1, 1)
        val task2 = Tasks(3, "string3", 0, 0, "24-03-2023", 1, 1, 1, 1)

        dao.insert(task)
        dao.insert(task1)
        dao.insert(task2)

        val tasksLiveData = dao.readAllData()

        val observer = Observer<List<Tasks>> { tasks ->
            assertEquals(3, tasks.size)
            assertEquals(task, tasks[0])
            assertEquals(task1, tasks[1])
            assertEquals(task2, tasks[2])
        }
    }

    //przechodzi, jeśli istniejący task o podanym id został pobrany z bazy
    @Test
    fun findByTaskID_taskExists() = runTest {
        val task = Tasks(1, "string", 0, 0, "24-03-2023", 1, 1, 1, 1)
        val task1 = Tasks(2, "string2", 0, 0, "24-03-2023", 1, 1, 1, 1)
        val task2 = Tasks(3, "string3", 0, 0, "24-03-2023", 1, 1, 1, 1)

        dao.insert(task)
        dao.insert(task1)
        dao.insert(task2)

        val result = dao.findTaskById(2).last()

        assertThat(result).isEqualTo(task1)
    }

    //przechodzi, jeśli żaden task nie został pobrany z bazy
    @Test
    fun findByTaskID_taskNotExists() = runTest {
        val task = Tasks(1, "string", 0, 0, "24-03-2023", 1, 1, 1, 1)
        val task2 = Tasks(3, "string3", 0, 0, "24-03-2023", 1, 1, 1, 1)

        dao.insert(task)
        dao.insert(task2)

        val result = dao.findTaskById(2)

        assertThat(result).isEmpty()
    }

    //Czytanie Taskow po przefiltrowaniu
    @Test
    fun readAllDataAsc() = runTest {
        val task = Tasks(1, "bac", 0, 0, "30-03-2023", 1, 1, 1, 1)
        val task2 = Tasks(2, "cab", 0, 0, "24-03-2023", 1, 1, 1, 1)
        val task3 = Tasks(3, "aac", 0, 0, "27-03-2023", 1, 1, 1, 1)

        dao.insert(task)
        dao.insert(task2)
        dao.insert(task3)

        val result = dao.readAllDataWithFilter()
        val exp = dao.readAllData();

        assertThat(exp).isNotEqualTo(result)
    }

    @Test
    fun getCurrentDays() = runTest {
        val task = Tasks(1, "bac", 0, 0, "31-03-2023", 1, 1, 1, 1)
        val task2 = Tasks(2, "cab", 0, 0, "31-03-2023", 1, 1, 1, 1)
        val task3 = Tasks(3, "aac", 0, 0, "27-03-2023", 1, 1, 1, 1)

        dao.insert(task)
        dao.insert(task2)
        dao.insert(task3)

        val result = dao.getCurrentDays()
        val act = dao.readAllData()

        assertThat(result).isEqualTo(act)
    }

}