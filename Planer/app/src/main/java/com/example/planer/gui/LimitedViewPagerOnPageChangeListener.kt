package com.example.planer.gui

import androidx.viewpager.widget.ViewPager

class LimitedViewPagerOnPageChangeListener(private val viewPager: ViewPager) : ViewPager.OnPageChangeListener {
    private var lastPosition = 0

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        // not used
    }

    override fun onPageSelected(position: Int) {
        if (position - lastPosition > 1) {
            // if the user tries to swipe more than 1 page forward, go back to the last position
            viewPager.setCurrentItem(lastPosition, true)
        } else {
            lastPosition = position
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        // not used
    }
}
