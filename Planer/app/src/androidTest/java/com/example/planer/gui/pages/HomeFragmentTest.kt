package com.example.planer.gui.pages

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import com.example.planer.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.accessibility.AccessibilityChecks
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.filters.LargeTest

@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeFragmentTest{


    //przechodzi, jeśli napis "DZIŚ" został poprawnie wyświetlony
    @Test
    fun lunchHomeFragmentAndVerify_today_title(){

        launchFragmentInContainer<HomeFragment>()

        onView(withId(R.id.today_title)).check(matches(withText("DZIŚ")))
    }

    //przechodzi, jeśli napis "JUTRO" został poprawnie wyświetlony
    @Test
    fun lunchHomeFragmentAndVerify_tomorrow_title(){

        launchFragmentInContainer<HomeFragment>()

        onView(withId(R.id.tomorrow_title)).check(matches(withText("JUTRO")))
    }

    //przechodzi, jeśli napis "DO KOŃCA TYGODNIA" został poprawnie wyświetlony
    @Test
    fun lunchHomeFragmentAndVerify_week_title(){

        launchFragmentInContainer<HomeFragment>()

        onView(withId(R.id.week_title)).check(matches(withText("DO KOŃCA TYGODNIA")))
    }

    //przechodzi, jeśli napis "DO KOŃCA MIESIĄCA" został poprawnie wyświetlony
    @Test
    fun lunchHomeFragmentAndVerify_month_title(){

        launchFragmentInContainer<HomeFragment>()

        onView(withId(R.id.month_title)).check(matches(withText("DO KOŃCA MIESIĄCA")))
    }

    //przechodzi, jeśli napis "CAŁA RESZTA" został poprawnie wyświetlony
    @Test
    fun lunchHomeFragmentAndVerify_rest_title(){

        launchFragmentInContainer<HomeFragment>()

        onView(withId(R.id.rest_title)).check(matches(withText("CAŁA RESZTA")))
    }

    //przechodzi, jeśli da się kliknąć na today_title
    @Test
    fun accessibilityTest_today_title(){

        launchFragmentInContainer<HomeFragment>()

        onView(withId(R.id.today_title)).check(matches(isClickable()))
    }

    //przechodzi, jeśli da się kliknąć na tomorrow_title
    @Test
    fun accessibilityTest_tomorrow_title(){

        launchFragmentInContainer<HomeFragment>()

        onView(withId(R.id.tomorrow_title)).check(matches(isClickable()))
    }

    //przechodzi jeśli da się kliknąć na week_title
    @Test
    fun accessibilityTest_week_title(){

        launchFragmentInContainer<HomeFragment>()

        onView(withId(R.id.week_title)).check(matches(isClickable()))
    }

    //przechodzi, jeśli da się kliknąć na month_title
    @Test
    fun accessibilityTest_month_title(){

        launchFragmentInContainer<HomeFragment>()

        onView(withId(R.id.month_title)).check(matches(isClickable()))
    }

    //przechodzi, jeśli da się kliknąć na rest_title
    @Test
    fun accessibilityTest_rest_title(){

        launchFragmentInContainer<HomeFragment>()

        onView(withId(R.id.rest_title)).check(matches(isClickable()))
    }
}