package com.example.planer.gui.pages

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import com.example.planer.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.isFocusable
import androidx.test.filters.LargeTest
import org.junit.Before

@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeFragmentTest{


    @Before
    fun setup(){
        launchFragmentInContainer<HomeFragment>()
    }

    //przechodzi, jeśli napis "DZIŚ" został poprawnie wyświetlony
    @Test
    fun lunchHomeFragmentAndVerify_today_title(){
        onView(withId(R.id.today_title)).check(matches(withText("DZIŚ")))
    }

    //przechodzi, jeśli napis "JUTRO" został poprawnie wyświetlony
    @Test
    fun lunchHomeFragmentAndVerify_tomorrow_title(){
        onView(withId(R.id.tomorrow_title)).check(matches(withText("JUTRO")))
    }

    //przechodzi, jeśli napis "DO KOŃCA TYGODNIA" został poprawnie wyświetlony
    @Test
    fun lunchHomeFragmentAndVerify_week_title(){
        onView(withId(R.id.week_title)).check(matches(withText("DO KOŃCA TYGODNIA")))
    }

    //przechodzi, jeśli napis "DO KOŃCA MIESIĄCA" został poprawnie wyświetlony
    @Test
    fun lunchHomeFragmentAndVerify_month_title(){
        onView(withId(R.id.month_title)).check(matches(withText("DO KOŃCA MIESIĄCA")))
    }

    //przechodzi, jeśli napis "CAŁA RESZTA" został poprawnie wyświetlony
    @Test
    fun lunchHomeFragmentAndVerify_rest_title(){
        onView(withId(R.id.rest_title)).check(matches(withText("CAŁA RESZTA")))
    }

    //przechodzi, jeśli da się kliknąć na today_title
    @Test
    fun accessibilityTest_today_title(){
        onView(withId(R.id.today_title)).check(matches(isClickable()))
    }

    //przechodzi, jeśli da się kliknąć na tomorrow_title
    @Test
    fun accessibilityTest_tomorrow_title(){
        onView(withId(R.id.tomorrow_title)).check(matches(isClickable()))
    }

    //przechodzi jeśli da się kliknąć na week_title
    @Test
    fun accessibilityTest_week_title(){
        onView(withId(R.id.week_title)).check(matches(isClickable()))
    }

    //przechodzi, jeśli da się kliknąć na month_title
    @Test
    fun accessibilityTest_month_title(){
        onView(withId(R.id.month_title)).check(matches(isClickable()))
    }

    //przechodzi, jeśli da się kliknąć na rest_title
    @Test
    fun accessibilityTest_rest_title(){
        onView(withId(R.id.rest_title)).check(matches(isClickable()))
    }


    /*
    UWAGA PRZED PRZEPROWADZNIEM PONIŻSZYCH TESTÓW NALEŻY PRZETESTOWAĆ KLASĘ AdapterTasks

    UWAGA PONIŻSZE TESTY SĄ JESZCZE DO DOPRACOWANIA
     */


    @Test
    fun recycleViewTest_today(){
        /*
        val recyclerView = R.id.today_task_list

        var list = emptyList<Tasks>()

        val adapter = AdapterTasks(
            list,
            {deleteId -> },
            {updateId -> }
        )

        recyclerView.adapter = adapter

        val tasks = listOf(
            Tasks(1, "task1", 1, 0, "24-03-2023", 1, 1, 1, 0),
            Tasks(2, "task2", 0, 0, "25-03-2023", 1, 0, 1, 1),
            Tasks(3, "task3", 0, 1, "26-03-2023", 2, 1, 0, 0)
        )

        adapter.setData(tasks)
*/

        onView(withId(R.id.today_task_list)).check(matches(isFocusable()))

    }


    @Test
    fun recycleViewTest_tomorrow(){
        onView(withId(R.id.tomorrow_task_list)).check(matches(isFocusable()))
    }

    @Test
    fun recycleViewTest_week(){
        onView(withId(R.id.week_task_list)).check(matches(isFocusable()))
    }

    @Test
    fun recycleViewTest_month(){
        onView(withId(R.id.month_task_list)).check(matches(isFocusable()))
    }

    @Test
    fun recycleViewTest_rest(){
        onView(withId(R.id.rest_task_list)).check(matches(isFocusable()))
    }
}