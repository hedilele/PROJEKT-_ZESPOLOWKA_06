package com.example.planer.gui.pages.home.habits

import android.content.res.ColorStateList
import android.util.Log
import android.view.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.entities.Habits
import com.example.planer.gui.callBacks.HabitDiffCallback
import kotlinx.android.synthetic.main.calendar_cell.view.*

import kotlinx.android.synthetic.main.single_habit.view.*

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
            updateListener(item)
            deleteListener(item)
        }

        if(item.isActive == 0)
        {
            val color = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.pr1_grey_habit))
            holder.itemView.habit_background.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.pr1_grey_habit))
        }
        else
        {
            val color = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.pr1_beige_background))
            holder.itemView.habit_background.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.pr1_beige_habit))
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