package com.example.planer.gui.pages.scope

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class UnscrollableLinearLayoutManager(context: Context) : LinearLayoutManager(context) {

    override fun canScrollVertically(): Boolean {
        return false
    }

    override fun canScrollHorizontally(): Boolean {
        return false
    }
}