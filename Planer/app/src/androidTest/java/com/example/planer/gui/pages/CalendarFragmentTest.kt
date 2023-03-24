package com.example.planer.gui.pages

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

import com.example.planer.R
@RunWith(AndroidJUnit4::class)
class CalendarFragmentTest{

    //TODO aby przeprowadzić ten test należy nadać przynajmniej jedno id w fragment_calendar.xml
    @Test
    fun lunchCalendarFragmentAndVerifyUI(){

        //funkcja odpala fragment
        launchFragmentInContainer<CalendarFragment>()


        //onView(withId(R.id.)).check(matches(withText("CALENDAR")))
    }


}