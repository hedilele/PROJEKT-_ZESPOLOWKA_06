package com.example.planer.gui.callBacks

import androidx.recyclerview.widget.DiffUtil
import com.example.planer.entities.Habits

class HabitDiffCallback(private val oldTasks: MutableList<Habits>, private val newTasks: MutableList<Habits>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldTasks.size
    }

    override fun getNewListSize(): Int {
        return newTasks.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTasks.elementAt(oldItemPosition).id == newTasks.elementAt(newItemPosition).id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTasks.elementAt(oldItemPosition) == newTasks.elementAt(newItemPosition)
    }
}
