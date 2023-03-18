package com.example.planer.gui

import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.UserViewModel
import com.example.planer.entities.Tasks
import kotlinx.android.synthetic.main.single_task.view.*

// klasa odpowiedzialna za umieszczanie pojedynczych tasków w recyclerView
class AdapterTasks(
    var list: List<Tasks>,
    private val deleteListener: (id: Int) -> Unit,
    private val updateListener: (id: Int) -> Unit
    ): RecyclerView.Adapter<AdapterTasks.ViewHolder>() {

    //var list = emptyList<Tasks>()
    private lateinit var userViewModel: UserViewModel

    var pos: Tasks? = null

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

        //pos = item

        holder.itemView.task_title.text = item.title
        holder.itemView.task_date.text = item.deadline
        //holder.itemView.task_box.

        /*
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

         */

        holder.itemView.trash.setOnClickListener {
            deleteListener(item.id)
        }

        holder.itemView.setOnClickListener{
        }


        //holder.itemView.findViewById<CardView>(R.id.task_list).startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.animation_recyclerview_load))




//        val popupMenu = PopupMenu(holder.itemView.context, holder.itemView.priority)
//
//        popupMenu.menu.add(Menu.NONE,0, 0, "edit")
//        popupMenu.menu.add(Menu.NONE,1, 1, "add")
//
//
//        holder.itemView.priority.setOnClickListener {
//            popupMenu.show()
//        }


        holder.itemView.name2.visibility = View.GONE

        holder.itemView.more_open.setOnClickListener {

            //holder.itemView.name2.setVisibility(View.VISIBLE)

            val transition: Transition = Slide(Gravity.RIGHT)
            transition.setDuration(500)
            transition.addTarget(holder.itemView.name2)

            TransitionManager.beginDelayedTransition(holder.itemView.parent_slide, transition)
            holder.itemView.name2.setVisibility(View.VISIBLE)
        }

        holder.itemView.more_close.setOnClickListener {

            //holder.itemView.name2.setVisibility(View.VISIBLE)

            val transition: Transition = Slide(Gravity.RIGHT)
            transition.setDuration(500)
            transition.addTarget(holder.itemView.name2)

            TransitionManager.beginDelayedTransition(holder.itemView.parent_slide, transition)
            holder.itemView.name2.setVisibility(View.INVISIBLE)
        }


    }


    fun setData(task: List<Tasks>){
        this.list = task
        notifyDataSetChanged()
    }


}