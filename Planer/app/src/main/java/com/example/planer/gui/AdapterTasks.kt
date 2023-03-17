package com.example.planer.gui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.UserViewModel
import com.example.planer.entities.Tasks
import kotlinx.android.synthetic.main.single_task.view.*

// klasa odpowiedzialna za umieszczanie pojedynczych tasków w recyclerView
class AdapterTasks(): RecyclerView.Adapter<AdapterTasks.ViewHolder>() {

    var list = emptyList<Tasks>()

    class ViewHolder(itemView: CardView): RecyclerView.ViewHolder(itemView) {  }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_task, parent, false) as CardView)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    // ustawianie wyglądu i zachowań tasków
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.itemView.task_title.text = item.title
        holder.itemView.task_date.text = item.deadline
        //holder.itemView.task_box.

        when (item.importance) {
            0 -> holder.itemView.priority?.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.neutral_blue
                )
            )
            1 -> holder.itemView.priority?.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.easy_green
                )
            )
            2 -> holder.itemView.priority?.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.medium_yellow
                )
            )
            3 -> holder.itemView.priority?.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.hard_red
                )
            )
        }

    }


    fun setData(task: List<Tasks>){
        this.list = task
        notifyDataSetChanged()
    }

}