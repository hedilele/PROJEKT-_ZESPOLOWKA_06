package com.top.planer.gui.callBacks

import androidx.recyclerview.widget.DiffUtil
import com.top.planer.entities.Tasks

class TaskDiffCallback(private val oldTasks: MutableList<Tasks>, private val newTasks: MutableList<Tasks>) : DiffUtil.Callback() {

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
