package com.example.planer.gui

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.entities.Calendar
import kotlinx.android.synthetic.main.calendar_cell.view.*
import java.time.LocalDate

class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>,
    var todayDay: String,
    var eventList: MutableList<Calendar>, //eventy z calego miesiaca
    private val onItemListener: OnItemListener,
    private val onLongItemListener: OnLongItemListener
) :
    RecyclerView.Adapter<CalendarViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.calendar_cell, parent, false)

        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.166666666).toInt()
        return CalendarViewHolder(view, onItemListener, onLongItemListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.dayOfMonth.setText(daysOfMonth[position])
        val todayDate = LocalDate.now().toString()


        if (todayDate.substring(1,7) == todayDay.substring(1,7) && todayDate.substring(8,10) == holder.itemView.cellDayText.text.toString()) //koloruje dzisiejszy dzien
        {
            val color = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
            holder.itemView.cellDayText.setTextColor(color)
        }

        if (todayDay.substring(8,10).trimStart('0') == holder.itemView.cellDayText.text.toString()) //koloruje klikniety dzien
        {
            holder.itemView.cellDayText.setTypeface(null, Typeface.BOLD)
            updateList(this.eventList)
        }


        val eventExists = eventList.find{event -> event.startDate.substring(8,10).trimStart('0')==daysOfMonth[position]}

        if (eventExists!=null)
        {
            val color = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.pr1_green_button))
            holder.itemView.cellDayText.backgroundTintList= color

        }

    }

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: String?)
    }

    interface OnLongItemListener {
        fun onLongItemClick(position: Int, dayText: String?)
    }


    fun updateList(newCalendar: MutableList<Calendar>) {
        val diffResult = DiffUtil.calculateDiff(
            com.example.planer.gui.callBacks.CalendarDiffCallback(
                this.eventList,
                newCalendar
            )
        )
        this.eventList = newCalendar
        diffResult.dispatchUpdatesTo(this)
    }

}
