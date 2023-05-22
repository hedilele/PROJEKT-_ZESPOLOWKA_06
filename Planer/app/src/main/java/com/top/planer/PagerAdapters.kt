package com.top.planer

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapters(sFM:FragmentManager)
    :FragmentPagerAdapter(sFM, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    // lista wszystkich fragmentów
    private val fragmentList = ArrayList<Fragment>()
    // lista tytułów fragmentów - może się przydać później

    // rozmiar tablicy z fragmentami
    override fun getCount(): Int = fragmentList.size

    // zwracanie fragmentów
    override fun getItem(position: Int): Fragment = fragmentList[position]

    // pobranie tytułu fragmentu
    // dodanie fragmentu do listy fragmentów
    fun addFragment(fm: Fragment) {
        fragmentList.add(fm)
    }
}

