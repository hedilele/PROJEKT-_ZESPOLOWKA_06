package com.example.planer.scope

import androidx.recyclerview.widget.DiffUtil
import com.example.planer.entities.relations.NoteAndTask

class NoteAndTaskDiffCallback(private val oldTasks: List<NoteAndTask>, private val newTasks: List<NoteAndTask>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldTasks.size
    }

    override fun getNewListSize(): Int {
        return newTasks.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTasks[oldItemPosition].tasks.id == newTasks[newItemPosition].tasks.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTasks[oldItemPosition] == newTasks[newItemPosition]
    }
}
