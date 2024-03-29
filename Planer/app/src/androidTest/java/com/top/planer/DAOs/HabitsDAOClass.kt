package com.top.planer.DAOs

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.top.planer.AppDatabase
import com.top.planer.entities.Habits
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class HabitsDAOClass {

    @get:Rule

    private lateinit var database: AppDatabase
    private lateinit var dao: HabitsDAO

    //inicjalizacja bazy i dao przed każdą funkcją @Test
    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.habitsDAO()
    }

    //zamykanie bazy po każdej funkcji @Test
    @After
    fun teardown(){
        database.close()
    }

    //jeżeli test przechodzi, to znaczy, że habit jest poprawnie dodawany i odczytywany z bazy
    @Test
    fun insertHabitsTest() = runTest{

        val habit = Habits(0, "common_habit", 1)
        dao.insert(habit)

        val h = dao.getHabitById(0)

        assertThat(h).isEqualTo(habit)
    }


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


    //zwraca true jeśli nie da się dodać habitsa bez nazwy
    //TODO nie przechodzi
    @Test
    fun insertHabitWithoutName() = runTest {
        val habit = Habits(0, "", 0)

        dao.insert(habit)

        val test = dao.getHabitById(0)

        assertThat(habit).isEqualTo(test);
    }

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

    //test przechodzi, gdy updatowanie habita, który nie istnieje nic nie zmienia
    //i w tym czasie baza jest pusta
    @Test
    fun updateDoesNotExitingHabitWithEmptyDB() = runTest {
        val habitNew =  Habits(0, "Ziemia", 1)

        dao.update(habitNew)

        val habitTest = dao.getHabitById(0)

        assertThat(habitTest).isNull()
    }

    //sprawdza czy można usuwać nazwę habitsa, nie powinno być takiej możliwości
    //TODO nie przechodzi
    @Test
    fun updateNameToEmpty() = runTest {
        val habitOld =  Habits(0, "Woda", 0)
        val habitNew =  Habits(0, "", 0)

        dao.insert(habitOld)

        dao.update(habitNew)

        val habitTest = dao.getHabitById(0)

        assertThat(habitTest.name).isNotEmpty()
    }

    //jeśli nie ma nic w bazie, to pobranie wszystkich elementów powinno zwrócić nic
    @Test
    fun getAllHabitsWithNoHabitsInDatabase() = runTest {
        val list = dao.getAllHabits()
        assertThat(list).isEmpty()
    }

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


    //test poprawności działania wczytywania habitsa po id
    @Test
    fun getHabitByIdTest() = runTest {
        val habit  = Habits(0, "dude", 1)

        dao.insert(habit)

        val test = dao.getHabitById(0)

        assertThat(test).isEqualTo(habit)
    }

    //sprawdza czy na pewno zwraca null gdy w bazie nie ma habitsa o podanym id
    @Test
    fun getHabitByIdFromEmptyDB() = runTest{
        val test = dao.getHabitById(0)
        assertThat(test).isNull()
    }

    //sprawdzenie czy usuwanie habitsa z bazy działa
    @Test
    fun deletingTest() = runTest {
        val habit = Habits(0, "hey", 1)

        dao.insert(habit)

        dao.delete(habit)

        val test = dao.getAllHabits()

        assertThat(test).isEmpty()
    }


    //sprawdzenie czy proba usuniecia habitsa o jedynie identycznym id się powiedzie
    //przechodzi gdy nie powinno
    //TODO nie przechodzi
    @Test
    fun  deletingById() = runTest {
        val habit = Habits(0, "hey", 1)
        val bomb = Habits(1, "bomb", 0)

        dao.insert(habit)

        //dao.delete(bomb)
        dao.insert(bomb)
        dao.deleteById(1)

        val test = dao.getAllHabits()

        assertThat(test).containsExactly(habit)
        //Dodalem osobna funkcje do Habits usuwajaca po samym id
    }


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


}