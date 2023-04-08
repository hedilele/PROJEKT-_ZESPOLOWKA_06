package com.example.planer.gui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.databinding.SingleTaskBinding
import com.example.planer.entities.Tasks

// Dominika usunie
class Adapter(
    private val items: ArrayList<Tasks>,
    private val updateListener: (id: Int) -> Unit,
    private val deleteListener: (id: Int) -> Unit,
    ) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    class ViewHolder(binding: SingleTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.taskTitle
        //val priority = binding.taskBox
        //TODO ZMIENIAM
        //val importance = binding.priority
        val deadline = binding.taskDate
        val trash = binding.done

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_task, parent, false)
//        return ViewHolder(itemView)
        return ViewHolder(
            SingleTaskBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]

        holder.title.text = item.title
        holder.deadline.text = item.deadline

        /*
        when (item.importance) {
            0 -> holder.importance?.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.neutral_blue
                )
            )
            1 -> holder.importance?.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.easy_green
                )
            )
            2 -> holder.importance?.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.medium_yellow
                )
            )
            3 -> holder.importance?.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.hard_red
                )
            )
        }

         */


        holder.trash.setOnClickListener {
            deleteListener(item.id)
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
}

