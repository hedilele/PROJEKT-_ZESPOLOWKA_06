package com.example.planer.gui

import android.annotation.SuppressLint
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.UserViewModel
import com.example.planer.entities.Tasks
import kotlinx.android.synthetic.main.single_task.view.*

// klasa odpowiedzialna za umieszczanie pojedynczych tasków w recyclerView
class AdapterTasks(
    var list: MutableList<Tasks>,
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
    @SuppressLint("ResourceAsColor")
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

        holder.itemView.done.setOnClickListener {
            deleteListener(item.id)

            //TODO wypisac komunikat z zapytaniem
        }

        holder.itemView.btn_delete.setOnClickListener {
            deleteListener(item.id)

            //TODO uruchomic algo?
        }

        holder.itemView.btn_edit.setOnClickListener {
            //var dialog = EditDialogFragment()
            val builder = AlertDialog.Builder(holder.itemView.context)
            //builder.setView(R.layout.activity_adding_task)

            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogView = inflater.inflate(R.layout.activity_adding_task, null)
            val textView = dialogView.findViewById<AppCompatEditText>(R.id.task_title)

            val btn_important0 = dialogView.findViewById<Button>(R.id.important0)
            val btn_important1 = dialogView.findViewById<Button>(R.id.important1)
            val btn_urgent0 = dialogView.findViewById<Button>(R.id.urgent0)
            val btn_urgent1 = dialogView.findViewById<Button>(R.id.urgent1)
            val btn_update = dialogView.findViewById<Button>(R.id.btn_create)

            builder.setView(dialogView)


            btn_important0.setOnClickListener {
                // Do something when the button is clicked, for example:
                //Toast.makeText(holder.itemView.context, "button clicked", Toast.LENGTH_SHORT)
                btn_urgent1.setTextColor(R.color.hard_red)
            }


            val alertDialog = builder.create()
            alertDialog.show()


//            //builder.setTitle("Dialog Title")
//            //builder.setMessage("Dialog Message for position $position")
//            builder.setPositiveButton("OK") { dialog, which ->
//                // Positive button click action
//            }
//            builder.setNegativeButton("Cancel") { dialog, which ->
//                // Negative button click action
//            }
            //builder.show()
        }





        //holder.itemView.findViewById<CardView>(R.id.task_list).startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.animation_recyclerview_load))



/*
        val popupMenu = PopupMenu(holder.itemView.context, holder.itemView.more_open)

        popupMenu.menu.add(Menu.NONE,0, 0, "edit")
        popupMenu.menu.add(Menu.NONE,1, 1, "add")


        holder.itemView.more_open.setOnClickListener {
            popupMenu.show()
        }
*/

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


    fun setData(task: MutableList<Tasks>){
        this.list = task
        notifyDataSetChanged()
    }


}