package com.example.planer.scope

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.entities.Notes
import com.example.planer.entities.Tasks

class CardAdapter(taskList: Map<Tasks, List<Notes>>) :
    RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    private var tasks: Map<Tasks, List<Notes>> = taskList

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView
        val deadlineText: TextView
        val noteText: TextView
        val laterButton: Button

        init {
            titleText = view.findViewById(R.id.titleTextView)
            deadlineText = view.findViewById(R.id.deadlineTextView)
            noteText = view.findViewById(R.id.noteTextView)
            laterButton = view.findViewById(R.id.postponeButton)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val oneTask = tasks.keys.elementAt(position)
        val oneNote: Notes? = tasks[oneTask]?.firstOrNull()

        viewHolder.titleText.text = oneTask.title
        viewHolder.deadlineText.text = oneTask.deadline
        viewHolder.noteText.text = oneNote?.noteContent ?: ""
    }

    override fun getItemCount(): Int = tasks.size

    fun updateList(newTasks: Map<Tasks, List<Notes>>) {
        val diffResult = DiffUtil.calculateDiff(TaskDiffCallback(tasks, newTasks))
        tasks = newTasks
        diffResult.dispatchUpdatesTo(this)
    }
}
