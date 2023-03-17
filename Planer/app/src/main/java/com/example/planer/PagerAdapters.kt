package com.example.planer

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.planer.gui.AddingTaskActivity


class PagerAdapters(sFM:FragmentManager)
    :FragmentPagerAdapter(sFM, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    /*set fragment Lists*/
    // lista wszystkich fragmentów
    private val fragmentList = ArrayList<Fragment>()

    // lista tytułów fragmentów - może się przydać później
    //private val fragmentTitle = ArrayList<String>()

    // rozmiar tablicy z fragmentami
    override fun getCount(): Int = fragmentList.size

    // zwracanie fragmentów
    override fun getItem(position: Int): Fragment = fragmentList[position]

    // pobranie tytułu fragmentu
    //override fun getPageTitle(position: Int): CharSequence = pFragmentTitle[position]

    // dodanie fragmentu do listy fragmentów
    fun addFragment(fm: Fragment) {
        fragmentList.add(fm)
        //fragmentTitle.add(title)
    }
}

