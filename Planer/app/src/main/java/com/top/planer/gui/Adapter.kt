package com.top.planer.gui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.top.planer.databinding.SingleTaskBinding
import com.top.planer.entities.Tasks

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

        holder.trash.setOnClickListener {
            deleteListener(item.id)
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
}

