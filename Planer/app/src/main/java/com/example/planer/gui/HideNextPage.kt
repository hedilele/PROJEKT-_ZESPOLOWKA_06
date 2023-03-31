package com.example.planer.gui

import android.view.View
import androidx.viewpager.widget.ViewPager


class HideNextPage : ViewPager.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        if (position > 0) {
            page.alpha = 0f // hide the next page
        } else {
            page.alpha = 1f // show the current page
        }
    }
}

