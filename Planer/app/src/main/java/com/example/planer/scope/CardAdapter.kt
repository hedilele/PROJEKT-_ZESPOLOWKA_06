package com.example.planer.scope

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.entities.relations.NoteAndTask
import timber.log.Timber

class CardAdapter(dataSet: List<NoteAndTask>) :
    RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    private var tasks: List<NoteAndTask> = dataSet

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView
        val deadlineText: TextView
        val noteText: TextView

        init {
            titleText = view.findViewById(R.id.titleTextView)
            deadlineText = view.findViewById(R.id.deadlineTextView)
            noteText = view.findViewById(R.id.noteTextView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = tasks[position]

        viewHolder.titleText.text = item.tasks.title
        viewHolder.deadlineText.text = item.tasks.deadline
        viewHolder.noteText.text = item.notes.noteContent
    }

    override fun getItemCount(): Int = tasks.size

    fun updateList(newTasks: List<NoteAndTask>) {
        val diffResult = DiffUtil.calculateDiff(TaskDiffCallback(tasks, newTasks))
        tasks = newTasks
        diffResult.dispatchUpdatesTo(this)
    }
}
