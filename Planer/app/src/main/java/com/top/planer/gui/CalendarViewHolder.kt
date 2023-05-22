package com.top.planer.gui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.top.planer.R

class CalendarViewHolder(itemView: View, onItemListener: CalendarAdapter.OnItemListener, onLongItemListener: CalendarAdapter.OnLongItemListener) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener{
    val dayOfMonth: TextView
    private val onItemListener: CalendarAdapter.OnItemListener
    private val onLongItemListener: CalendarAdapter.OnLongItemListener

    init {
        dayOfMonth = itemView.findViewById(R.id.cellDayText)
        this.onItemListener = onItemListener
        this.onLongItemListener = onLongItemListener
        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }

    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, dayOfMonth.text as String)
    }

    override fun onLongClick(v: View?): Boolean {
        onLongItemListener.onLongItemClick(adapterPosition, dayOfMonth.text as String)
        return true
    }


}
