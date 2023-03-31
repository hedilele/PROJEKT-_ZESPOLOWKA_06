package com.example.planer.gui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


class LimitedPages(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {


    private var maxPagesToSwipe = 1
    private var currentPosition = 0

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.actionMasked == MotionEvent.ACTION_DOWN) {
            // reset the maximum number of pages that can be swiped
            maxPagesToSwipe = 1
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        // check if the user tries to swipe more than the allowed number of pages
        if (Math.abs(item - currentPosition) <= maxPagesToSwipe) {
            super.setCurrentItem(item, smoothScroll)
            currentPosition = item
        }
    }


}