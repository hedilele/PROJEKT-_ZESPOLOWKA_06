package com.example.planer.DAOs

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
import kotlinx.coroutines.launch
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

    /*
    @Test
    fun insertTask() = runTest{
        val task = Tasks(0, "common_task", 2, 1,  "15-03-2023", 1, 1, 1, 1)
        dao.insert(task)

        var tas = Tasks(-1, "", -1, -1,  "=", -1, -1, -1, -1)

        //var lifecycleScope: CoroutineScope
        val scope: CoroutineScope? = null
        scope?.launch {
           dao.findTaskById(0).collect { t ->

               tas = t
            }
        }
        scope?.cancel()

        assertThat(tas).isEqualTo(task)
    }*/
}