package com.top.planer.gui.callBacks

import androidx.recyclerview.widget.DiffUtil
import com.top.planer.entities.Calendar

class CalendarDiffCallback (private val oldCalendar: MutableList<Calendar>, private val newCalendar: MutableList<Calendar>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldCalendar.size
    }

    override fun getNewListSize(): Int {
        return newCalendar.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCalendar.elementAt(oldItemPosition).id == newCalendar.elementAt(newItemPosition).id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCalendar.elementAt(oldItemPosition) == newCalendar.elementAt(newItemPosition)
    }
}