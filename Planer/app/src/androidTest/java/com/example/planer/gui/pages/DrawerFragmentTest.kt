package com.example.planer.gui.pages

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import com.example.planer.R
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DrawerFragmentTest{

    //TODO aby przeprowadzić ten test należy nadać przynajmniej jedno id w fragment_drawer.xml
    @Test
    fun lunchDrawerFragmentAndVerifyUI(){

        //funkcja odpala fragment
        launchFragmentInContainer<DrawerFragment>()

        //onView(withId(R.id.)).check(matches(withText("drawer chyba")))
    }

}