package com.example.planer.gui.pages.scope

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.entities.Notes
import com.example.planer.entities.Tasks
import com.example.planer.entities.Types
import kotlinx.android.synthetic.main.card.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CardAdapter(taskList: Map<Tasks, List<Notes>>, typeList: List<Types>) :
    RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    private var tasks: Map<Tasks, List<Notes>> = taskList
    private var types: List<Types> = typeList
    private var onButtonClickListener: OnButtonClickListener? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView
        val deadlineText: TextView
        val noteText: TextView
        val laterButton: Button
        val doneButton: Button

        init {
            titleText = view.findViewById(R.id.titleTextView)
            deadlineText = view.findViewById(R.id.deadlineTextView)
            noteText = view.findViewById(R.id.noteTextView)
            laterButton = view.findViewById(R.id.postponeButton)
            doneButton = view.findViewById(R.id.doneButton)
        }
    }

    interface OnButtonClickListener {
        suspend fun onLaterButtonClick(item: Tasks)
        suspend fun onDoneButtonClick(task: Tasks, note: Notes?)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val oneTask = tasks.keys.elementAt(position)
        val oneNote: Notes? = tasks[oneTask]?.firstOrNull()
        val itemsType: Types? = types.find { type -> type.id == oneTask.typeId }
        val drawable = viewHolder.itemView.scope_card.background as GradientDrawable
        val color =
            if (itemsType != null) Color.parseColor(itemsType.colour) else ContextCompat.getColor(
                viewHolder.itemView.context,
                R.color.brown_important_urgent_off
            )
        drawable.setStroke(5, color)

        viewHolder.titleText.text = oneTask.title
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val deadlineDateTime = LocalDateTime.parse(oneTask.deadline, formatter)
        val deadlineText =
            "Deadline: " + deadlineDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))
        viewHolder.deadlineText.text = deadlineText
        val noteTmp =
            if (oneNote?.noteContent != null && oneNote.noteContent != "") "Notatka:\n${oneNote.noteContent}" else ""
        viewHolder.noteText.text = noteTmp

        viewHolder.laterButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                onButtonClickListener?.onLaterButtonClick(oneTask)
            }
        }

        viewHolder.doneButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                onButtonClickListener?.onDoneButtonClick(oneTask, oneNote)
            }
        }
    }

    fun setOnItemClickListener(listener: OnButtonClickListener) {
        onButtonClickListener = listener
    }

    override fun getItemCount(): Int = tasks.size

    fun updateList(newTasks: Map<Tasks, List<Notes>>) {
        val diffResult = DiffUtil.calculateDiff(TaskDiffCallback(tasks, newTasks))
        tasks = newTasks
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateTypes(newTypes: List<Types>) {
        types = newTypes
    }
}
