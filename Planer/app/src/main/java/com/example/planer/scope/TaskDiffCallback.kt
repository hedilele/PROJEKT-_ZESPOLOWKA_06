package com.example.planer.scope

import androidx.recyclerview.widget.DiffUtil
import com.example.planer.entities.Notes
import com.example.planer.entities.Tasks

class TaskDiffCallback(private val oldTasks: Map<Tasks, List<Notes>>, private val newTasks: Map<Tasks, List<Notes>>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldTasks.size
    }

    override fun getNewListSize(): Int {
        return newTasks.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTasks.keys.elementAt(oldItemPosition).id == newTasks.keys.elementAt(newItemPosition).id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTasks.keys.elementAt(oldItemPosition) == newTasks.keys.elementAt(newItemPosition)
    }
}
