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
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.ViewModel.UserViewModel
import com.example.planer.entities.Habits
import com.example.planer.entities.Tasks
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_adding_task.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.single_task.view.*
import kotlinx.android.synthetic.main.single_task.view.task_title
import java.util.*

// klasa odpowiedzialna za umieszczanie pojedynczych tasków w recyclerView
class AdapterHabits(
    var list: MutableList<Habits>,
    private val deleteListener: (id: Int) -> Unit,
    private val updateListener: (habit: Habits) -> Unit
): RecyclerView.Adapter<AdapterHabits.ViewHolder>() {

    private lateinit var userViewModel: UserViewModel


    class ViewHolder(itemView: CardView): RecyclerView.ViewHolder(itemView) {  }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_habit, parent, false) as CardView)

    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val iv = holder.itemView


        iv.edit_drawer.visibility = View.GONE


        iv.more_open.setOnClickListener {

            //holder.itemView.name2.setVisibility(View.VISIBLE)

            val transition: Transition = Slide(Gravity.RIGHT)
            transition.setDuration(500)
            transition.addTarget(iv.edit_drawer)

            TransitionManager.beginDelayedTransition(iv.parent_slide, transition)
            iv.edit_drawer.setVisibility(View.VISIBLE)
        }

        iv.more_close.setOnClickListener {

            //holder.itemView.name2.setVisibility(View.VISIBLE)

            val transition: Transition = Slide(Gravity.RIGHT)
            transition.setDuration(500)
            transition.addTarget(iv.edit_drawer)

            TransitionManager.beginDelayedTransition(iv.parent_slide, transition)
            iv.edit_drawer.setVisibility(View.INVISIBLE)
        }


    }

    fun setData(habit: MutableList<Habits>){
        this.list = habit
        notifyDataSetChanged()
    }


}