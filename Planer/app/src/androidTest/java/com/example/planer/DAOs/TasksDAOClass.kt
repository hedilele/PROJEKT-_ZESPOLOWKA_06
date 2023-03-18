package com.example.planer.DAOs

import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.planer.AppDatabase
import com.example.planer.entities.Habits
import com.example.planer.entities.Tasks
import com.example.planer.tasks.Task
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Flow


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



    //jeżeli test przechodzi, to znaczy, że habit jest poprawnie dodawany i odczytywany z bazy
    @Test
    fun insertTaskTest() = runTest{

        val task = Tasks(1, "common_task", 2, 2, "27-03-2023",  1, 1, 1, 1)

        dao.insert(task)

        val dbFlow = flowOf(listOf(task))

        val result = dao.fetchAll()

        assertThat(result).isEqualTo(dbFlow)
    }


    /*

    //sprawdzanie czy można dodać dwa habitsy o tym samym id
    //przechodzi, jeśli nie można
    @Test
    fun insertSameIdTwice() = runTest {
        val habit1 = Habits(0, "common_habit", 1)
        val habit2 = Habits(0, "unusual_habit", 1)
        val testList = listOf(habit1, habit2)

        dao.insert(habit1)
        dao.insert(habit2)

        val trueList = dao.getAllHabits()

        assertThat(trueList).isNotEqualTo(testList)
    }
     */

    /*

    //zwraca true jeśli nie da się dodać habitsa bez nazwy
    @Test
    fun insertHabitWithoutName() = runTest {
        val habit = Habits(0, "", 0)

        dao.insert(habit)

        val test = dao.getHabitById(0)

        assertThat(habit).isNotEqualTo(test)
    }
        */
    /*

    //sprawdza czy dao poprawnie updatuje habitsa
    //test przechodzi gdy pobrany po id habit jest taki jaki powinien byc po zaktualizowaniu
    @Test
    fun updateFirstTest() = runTest {
        val habitOld =  Habits(0, "Woda", 0)
        val habitNew =  Habits(0, "Ziemia", 1)

        dao.insert(habitOld)

        dao.update(habitNew)

        val habitTest = dao.getHabitById(0)

        assertThat(habitTest).isEqualTo(habitNew)
    }
        */
    /*

    //sprawdza czy na pewno habit jest updatowany, czy nie dodaje drugiego o takim samym id
    @Test
    fun updateSecondTest() = runTest {
        val habitOld =  Habits(0, "Woda", 0)
        val habitNew =  Habits(0, "Ziemia", 1)

        dao.insert(habitOld)

        dao.update(habitNew)

        val list = dao.getAllHabits()

        assertThat(list).containsExactly(habitNew)
    }
        */
    /*

    //test przechodzi, gdy updatowanie habita, który nie istnieje nic nie zmienia
    @Test
    fun updateDoesNotExitingHabit() = runTest {
        val habitOld =  Habits(0, "Woda", 0)
        val habitNew =  Habits(1, "Ziemia", 1)

        dao.insert(habitOld)

        dao.update(habitNew)

        val habitTest = dao.getHabitById(1)

        assertThat(habitTest).isNull()
    }
        */
    /*

    //test przechodzi, gdy updatowanie habita, który nie istnieje nic nie zmienia
    //i w tym czasie baza jest pusta
    @Test
    fun updateDoesNotExitingHabitWithEmptyDB() = runTest {
        val habitNew =  Habits(0, "Ziemia", 1)

        dao.update(habitNew)

        val habitTest = dao.getHabitById(0)

        assertThat(habitTest).isNull()
    }
        */
    /*

    //sprawdza czy można usuwać nazwę habitsa, nie powinno być takiej możliwości
    @Test
    fun updateNameToEmpty() = runTest {
        val habitOld =  Habits(0, "Woda", 0)
        val habitNew =  Habits(0, "", 0)

        dao.insert(habitOld)

        dao.update(habitNew)

        val habitTest = dao.getHabitById(0)

        assertThat(habitTest.name).isNotEmpty()
    }
        */
    /*

    //jeśli nie ma nic w bazie, to pobranie wszystkich elementów powinno zwrócić nic
    @Test
    fun getAllHabitsWithNoHabitsInDatabase() = runTest {
        val list = dao.getAllHabits()
        assertThat(list).isEmpty()
    }
        */
    /*

    //sprawdza czy pobieranie wszystkich elementow działa
    @Test
    fun getAllHabitsTest() = runTest {
        val habit1 = Habits(0, "woda", 1)
        val habit2 = Habits(1, "ziemia", 1)
        val habit3 = Habits(2, "ogień", 0)

        dao.insert(habit1)
        dao.insert(habit2)
        dao.insert(habit3)

        val list = dao.getAllHabits()

        assertThat(list).containsExactlyElementsIn(listOf(habit1, habit2, habit3))
    }
        */

    /*

    //test poprawności działania wczytywania habitsa po id
    @Test
    fun getHabitByIdTest() = runTest {
        val habit  = Habits(0, "dude", 1)

        dao.insert(habit)

        val test = dao.getHabitById(0)

        assertThat(test).isEqualTo(habit)
    }
        */
    /*

    //sprawdza czy na pewno zwraca null gdy w bazie nie ma habitsa o podanym id
    @Test
    fun getHabitByIdFromEmptyDB() = runTest{
        val test = dao.getHabitById(0)
        assertThat(test).isNull()
    }
        */
    /*

    //sprawdzenie czy usuwanie habitsa z bazy działa
    @Test
    fun deletingTest() = runTest {
        val habit = Habits(0, "hey", 1)

        dao.insert(habit)

        dao.delete(habit)

        val test = dao.getAllHabits()

        assertThat(test).isEmpty()
    }
        */

    /*

    //sprawdzenie czy proba usuniecia habitsa o jedynie identycznym id się powiedzie
    //przechodzi gdy nie powinno
    @Test
    fun  deletingById() = runTest {
        val habit = Habits(0, "hey", 1)
        val bomb = Habits(0, "bomb", 0)

        dao.insert(habit)

        dao.delete(bomb)

        val test = dao.getAllHabits()

        assertThat(test).containsExactly(habit)
    }
        */

    /*

    //sprawdzenie czy proba usuniecia habitsa o jedynie identycznym name się powiedzie
    //przechodzi gdy nie powinno
    @Test
    fun deletingByName() = runTest {
        val habit = Habits(0, "hey", 1)
        val bomb = Habits(1, "hey", 0)

        dao.insert(habit)

        dao.delete(bomb)

        val test = dao.getAllHabits()

        assertThat(test).containsExactly(habit)
    }
        */
    /*

    //sprawdzenie czy usunięcie habitsa o jedynie identycznym isActive sie powiedzie
    //przechodzi gdy nie powinno
    @Test
    fun deletingByActive() = runTest {
        val habit = Habits(0, "hey", 1)
        val bomb = Habits(1, "bomb", 1)

        dao.insert(habit)

        dao.delete(bomb)

        val test = dao.getAllHabits()

        assertThat(test).containsExactly(habit)
    }
        */
    /*

    //sprawdzenie czy na pewno usuwa wskazany habit i nic więcej
    @Test
    fun deletingOnlyOneHabit() = runTest{
        val habit1 = Habits(0, "woda", 1)
        val habit2 = Habits(1, "ziemia", 1)
        val habit3 = Habits(2, "ogień", 0)

        dao.insert(habit1)
        dao.insert(habit2)
        dao.insert(habit3)

        dao.delete(habit2)

        val test = dao.getAllHabits()

        assertThat(test).containsExactlyElementsIn(listOf(habit1, habit3))
    }
        */
    /*

    //test funkcji usuwającej wszystkie habitsy z bazy
    @Test
    fun deleteAllHabits() = runTest {
        val habit1 = Habits(0, "woda", 1)
        val habit2 = Habits(1, "ziemia", 1)
        val habit3 = Habits(2, "ogień", 0)

        dao.insert(habit1)
        dao.insert(habit2)
        dao.insert(habit3)

        dao.deleteAll()

        val test = dao.getAllHabits()

        assertThat(test).isEmpty()
    }
        */




}