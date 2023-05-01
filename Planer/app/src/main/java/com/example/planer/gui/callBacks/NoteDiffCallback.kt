package com.example.planer.gui.callBacks

import androidx.recyclerview.widget.DiffUtil
import com.example.planer.entities.Notes
import com.example.planer.entities.Tasks

class NoteDiffCallback(private val oldNotes: MutableList<Notes>, private val newNotes: MutableList<Notes>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldNotes.size
    }

    override fun getNewListSize(): Int {
        return newNotes.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNotes.elementAt(oldItemPosition).noteId == newNotes.elementAt(newItemPosition).noteId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNotes.elementAt(oldItemPosition) == newNotes.elementAt(newItemPosition)
    }
}
