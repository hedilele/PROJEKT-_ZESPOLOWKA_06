package com.example.planer.gui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Handler
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.ViewModel.UserViewModel
import com.example.planer.entities.Habits
import com.example.planer.entities.Tasks
import com.example.planer.gui.callBacks.HabitDiffCallback
import com.example.planer.habits.Habit

import kotlinx.android.synthetic.main.single_habit.view.*
import kotlinx.android.synthetic.main.single_task.view.*

// klasa odpowiedzialna za umieszczanie pojedynczych habitsow w recyclerView
class AdapterHabits(
    var list: MutableList<Habits>,
    private val deleteListener: (Habits) -> Unit,
    private val updateListener: (Habits) -> Unit
): RecyclerView.Adapter<AdapterHabits.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }


    fun updateList(newHabits: MutableList<Habits>) {
        val diffResult = DiffUtil.calculateDiff(
            HabitDiffCallback(this.list, newHabits)
        )
        this.list = newHabits
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_habit, parent, false) as CardView
        )

    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val iv = holder.itemView

        holder.itemView.habit_title.setText(item.name)

        holder.itemView.setOnClickListener {
            deleteListener(item)
        }


    }
}


/*


    class ViewHolder(itemView: CardView): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_habit, parent, false) as CardView)

    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val iv = holder.itemView

        holder.itemView.habit_title.setText(item.name)

        holder.itemView.setOnClickListener {
            deleteListener(holder.itemView.id)
        }



        }

 */