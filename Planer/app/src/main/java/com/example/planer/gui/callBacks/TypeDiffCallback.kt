package com.example.planer.gui.callBacks

import androidx.recyclerview.widget.DiffUtil
import com.example.planer.entities.Types

class TypeDiffCallback(private val oldTypes: List<Types>, private val newTypes: List<Types>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldTypes.size
    }

    override fun getNewListSize(): Int {
        return newTypes.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTypes.elementAt(oldItemPosition).id == newTypes.elementAt(newItemPosition).id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTypes.elementAt(oldItemPosition) == newTypes.elementAt(newItemPosition)
    }
}