package com.example.planer.gui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.PorterDuff
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
import com.example.planer.entities.Tasks
import com.example.planer.tasks.Task
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_adding_task.view.*
import kotlinx.android.synthetic.main.dialog_task_info.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.single_task.view.*
import kotlinx.android.synthetic.main.single_task.view.task_title
import java.util.*

// klasa odpowiedzialna za umieszczanie pojedynczych tasków w recyclerView
class AdapterTasks(
    var list: MutableList<Tasks>,
    private val deleteListener: (id: Int) -> Unit,
    private val updateListener: (task: Tasks, note: String) -> Unit
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
        // 0123456789012345
        // yyyy-mm-dd hh:mm
        holder.itemView.task_date.text =
            item.deadline.substring(8, 10) + '.' +
            item.deadline.substring(5, 7) + '.' +
            item.deadline.substring(0, 4) +' ' +
            item.deadline.substring(11)

        holder.itemView.done.setOnClickListener {

            var clicked = 0

            //holder.itemView.visibility = View.GONE
            //holder.itemView.done.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.icon_checkbox_filled))

            //TODO wypisac komunikat z zapytaniem
            val snackbar = Snackbar.make(holder.itemView, "Czy chcesz cofnąc usunięcie?", Snackbar.LENGTH_LONG)
            snackbar.duration = 2000
            snackbar.setAction("cofnij") {
                clicked = 1
                //holder.itemView.visibility = View.VISIBLE
                //holder.itemView.done.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.icon_checkbox_empty))


            }

            snackbar.show()
            holder.itemView.done.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.icon_checkbox_filled))
            holder.itemView.done.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off), PorterDuff.Mode.SRC_ATOP)

            Handler().postDelayed({
                if(clicked == 0)
                {
                    deleteListener(item.id)
                }
                holder.itemView.done.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.icon_checkbox_empty))
                //holder.itemView.done.setColorFilter(R.color.brown_important_urgent_off)
                holder.itemView.done.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off), PorterDuff.Mode.SRC_ATOP)

            }, 2000) // opóźnienie wynosi 5000 milisekund, czyli 5 sekund

        }


        holder.itemView.btn_delete.setOnClickListener {
            deleteListener(item.id)

            //TODO uruchomic algo?
        }

        holder.itemView.btn_edit.setOnClickListener {

            var title: String
            var importance: Int = item.importance
            var urgency: Int = item.urgency
            var deadline_day: String        // YYYY-MM-DD
            var deadline_time: String       // HH:MM


            var duration: Int = item.timeToFinish
            var isActive: Int = 0
            var typeId: Int = item.typeId
            var noteId: Int = 0
            var note: String = ""




            //var dialog = EditDialogFragment()
            val builder = AlertDialog.Builder(holder.itemView.context) //TODO
            //builder.setView(R.layout.activity_adding_task)

            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogView = inflater.inflate(R.layout.activity_editing_task, null)
            builder.setView(dialogView)
            val textView = dialogView.findViewById<AppCompatEditText>(R.id.task_title)

            val btn_important0 = dialogView.findViewById<Button>(R.id.important0)
            val btn_important1 = dialogView.findViewById<Button>(R.id.important1)
            val btn_urgent0 = dialogView.findViewById<Button>(R.id.urgent0)
            val btn_urgent1 = dialogView.findViewById<Button>(R.id.urgent1)
            val btn_update = dialogView.findViewById<Button>(R.id.btn_create)

            builder.setView(dialogView) //Podlaczanie xmla


            val important0 = dialogView.findViewById<Button>(R.id.important0)
            val important1 = dialogView.findViewById<Button>(R.id.important1)
            val urgent0 = dialogView.findViewById<Button>(R.id.urgent0)
            val urgent1 = dialogView.findViewById<Button>(R.id.urgent1)

            val task_title = dialogView.findViewById<AppCompatEditText>(R.id.task_title)

            val day = dialogView.findViewById<TextView>(R.id.tv_deadline_d)
            val month = dialogView.findViewById<TextView>(R.id.tv_deadline_m)
            val year = dialogView.findViewById<TextView>(R.id.tv_deadline_y)
            val hour = dialogView.findViewById<TextView>(R.id.tv_deadline_h)
            val minutes = dialogView.findViewById<TextView>(R.id.tv_deadline_min)

            val calendar = dialogView.findViewById<ImageView>(R.id.btn_deadline)
            val clock = dialogView.findViewById<ImageView>(R.id.btn_deadline_time)

            val duration1 = dialogView.findViewById<TextView>(R.id.duration1)
            val duration2 = dialogView.findViewById<TextView>(R.id.duration2)
            val duration3 = dialogView.findViewById<TextView>(R.id.duration3)
            val duration4 = dialogView.findViewById<TextView>(R.id.duration4)
            val duration5 = dialogView.findViewById<TextView>(R.id.duration5)
            val duration6 = dialogView.findViewById<TextView>(R.id.duration6)

            val type1 = dialogView.findViewById<TextView>(R.id.type1)
            val type2 = dialogView.findViewById<TextView>(R.id.type2)
            val type3 = dialogView.findViewById<TextView>(R.id.type3)
            val type4 = dialogView.findViewById<TextView>(R.id.type4)

            val task_note = dialogView.findViewById<AppCompatEditText>(R.id.note)

            val cancel = dialogView.findViewById<Button>(R.id.btn_cancel)
            val edit = dialogView.findViewById<Button>(R.id.btn_create)
            edit.setText("Edytuj")


            //ustawianie wartosci taska
            task_title.setText(item.title)

            if(item.importance == 0)
            {
                importance = 0
                important0.setBackgroundColor(R.color.brown_important_urgent_on)
            }
            else
            {
                importance = 1
                important1.setBackgroundColor(R.color.brown_important_urgent_on)
            }

            if(item.urgency == 0)
            {
                urgency = 0
                urgent0.setBackgroundColor(R.color.brown_important_urgent_on)
            }
            else
            {
                urgency = 1
                urgent1.setBackgroundColor(R.color.brown_important_urgent_on)
            }


            deadline_day = item.deadline.substring(0, 10)
            deadline_time = item.deadline.substring(11)

            val tab_day = deadline_day.split('-')
            val tab_time = deadline_time.split(':')

            day.setText(tab_day[2])
            month.setText(tab_day[1])
            year.setText(tab_day[0])

            hour.setText(tab_time[0])
            minutes.setText(tab_time[1])





            fun uncheckImportance()
            {
                when(importance)
                {
                    0 -> {
                        important0.setBackgroundColor(android.R.color.transparent)
                    }
                    1 -> {
                        important1.setBackgroundColor(android.R.color.transparent)
                    }
                }

            }

            fun uncheckUrgency()
            {
                when(urgency)
                {
                    0 -> {
                        urgent0.setBackgroundColor(android.R.color.transparent)
                    }
                    1 -> {
                        urgent1.setBackgroundColor(android.R.color.transparent)
                    }
                }

            }


            important0.setOnClickListener {
                uncheckImportance()
                important0.setBackgroundColor(R.color.brown_important_urgent_on)
                importance = 0
            }

            important1.setOnClickListener {
                uncheckImportance()
                important1.setBackgroundColor(R.color.brown_important_urgent_on)
                importance = 1
            }

            urgent0.setOnClickListener {
                uncheckUrgency()
                urgent0.setBackgroundColor(R.color.brown_important_urgent_on)
                urgency = 0
            }

            urgent1.setOnClickListener {
                uncheckUrgency()
                urgent1.setBackgroundColor(R.color.brown_important_urgent_on)
                urgency = 1
            }

            fun setUpDate(d: Int, m: Int, y: Int): String {
                var month = m.toString()
                var day = d.toString()

                month.takeIf { m + 1 < 10 }?.let { month = "0" + (m + 1) }
                day.takeIf { d < 10 }?.let { day = "0" + d }

                return "$y-$month-$day"
            }

            fun setDateBlocks(date: String) {
                val table = date.split('-')
                day.setText(table[2])
                month.setText(table[1])
                year.setText(table[0])
            }

            // formatowanie godziny
            fun setUpTime(h: Int, m: Int): String {
                var hour = h.toString()
                var minutes = m.toString()

                hour.takeIf { h < 10 }?.let { hour = "0" + (h) }
                minutes.takeIf { m < 10 }?.let { minutes = "0" + m }

                return "$hour:$minutes"
            }


            // wpisanie aktualnej godziny do pól w tworzeniu tasków - takie ustalenie domyślej godziny przy tworzeniu
            fun setTimeBlocks(date: String) {
                val table = date.split(':')
                hour.setText(table[0])
                minutes.setText(table[1])
            }



            calendar.setOnClickListener {
                val cal = Calendar.getInstance()
                val today_year = cal.get(Calendar.YEAR)
                val today_month = cal.get(Calendar.MONTH)
                val today_day = cal.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(
                    holder.itemView.context,
                    DatePickerDialog.OnDateSetListener { view, sel_year, sel_month, sel_day ->

                        deadline_day = setUpDate(sel_day, sel_month, sel_year)
                        setDateBlocks(deadline_day)



                    }, today_year, today_month, today_day
                )

                dpd.show()
            }


            clock.setOnClickListener {

                val cal = Calendar.getInstance()
                val today_hour = cal.get(Calendar.HOUR)
                val today_minute = cal.get(Calendar.MINUTE)

                var tpd = TimePickerDialog(
                    holder.itemView.context,
                    TimePickerDialog.OnTimeSetListener { view, sel_hour, sel_minutes ->

                        deadline_time = setUpTime(sel_hour, sel_minutes)
                        setTimeBlocks(deadline_time)

                    }, today_hour, today_minute,
                    true
                )
                tpd.show()
            }



            deadline_day =
                year.text.toString() + '-' +
                month.text.toString() + '-' +
                day.text.toString()

            deadline_time =
                hour.text.toString() + ':' +
                minutes.text.toString()


            //TODO konkretna data czy deadline


            fun uncheckDuration()
            {
                when(duration)
                {
                    1 -> {
                        duration1.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }
                    2 -> {
                        duration2.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }
                    3 -> {
                        duration3.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }
                    4 -> {
                        duration4.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }
                    5 -> {
                        duration5.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }
                    6 -> {
                        duration6.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }

                }

            }

            duration1.setOnClickListener {
                uncheckDuration()
                duration1.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                duration = 1
            }

            duration2.setOnClickListener {
                uncheckDuration()
                duration2.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                duration = 2
            }

            duration3.setOnClickListener {
                uncheckDuration()
                duration3.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                duration = 3
            }

            duration4.setOnClickListener {
                uncheckDuration()
                duration4.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                duration = 4
            }

            duration5.setOnClickListener {
                uncheckDuration()
                duration5.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                duration = 5
            }

            duration6.setOnClickListener {
                uncheckDuration()
                duration6.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                duration = 6
            }

            fun uncheckType()
            {
                when(typeId)
                {
                    1 -> {
                        type1.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }
                    2 -> {
                        type2.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }
                    3 -> {
                        type3.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }
                    4 -> {
                        type4.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }

                }

            }

            type1.setOnClickListener {
                uncheckType()
                type1.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                typeId = 1
            }

            type2.setOnClickListener {
                uncheckType()
                type2.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                typeId = 2
            }

            type3.setOnClickListener {
                uncheckType()
                type3.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                typeId = 3
            }

            type4.setOnClickListener {
                uncheckType()
                type4.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                typeId = 4
            }

            if(!task_note.text.toString().isEmpty())
            {
                note = task_note.text.toString()
            }
            else
            {
                note = ""       //??????
            }


            val alertDialog = builder.create()
            alertDialog.show()

            cancel.setOnClickListener {
                alertDialog.hide()
            }

            edit.setOnClickListener {
                var task = Tasks(
                    item.id,
                    task_title.text.toString(),
                    importance,
                    urgency,
                    deadline_day+' '+deadline_time,
                    duration,
                    isActive,
                    typeId,
                    0,
                    date = null
                )

                updateListener(task, note)

                //alertDialog.hide()
                alertDialog.cancel()

            }






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

        holder.itemView.setOnClickListener{

            val builder = AlertDialog.Builder(holder.itemView.context) //TODO
            //builder.setView(R.layout.activity_adding_task)

            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogView = inflater.inflate(R.layout.dialog_task_info, null)
            builder.setView(dialogView)

            val title = dialogView.findViewById<TextView>(R.id.title)
            val date = dialogView.findViewById<TextView>(R.id.date)
            val important = dialogView.findViewById<TextView>(R.id.important)
            val urgent = dialogView.findViewById<TextView>(R.id.urgent)
            val type = dialogView.findViewById<TextView>(R.id.type)
            val duration = dialogView.findViewById<TextView>(R.id.duration)
            val note = dialogView.findViewById<TextView>(R.id.note_content)


            builder.setView(dialogView) //Podlaczanie xmla

            title.setText(holder.itemView.task_title.text.toString())
            date.setText(holder.itemView.task_date.text.toString())
            //important.setText(holder.itemView.important.text.toString())

            val alertDialog = builder.create()
            alertDialog.show()

        }



        holder.itemView.edit_drawer.visibility = View.GONE


        holder.itemView.more_open.setOnClickListener {

            //holder.itemView.name2.setVisibility(View.VISIBLE)

            val transition: Transition = Slide(Gravity.RIGHT)
            transition.setDuration(500)
            transition.addTarget(holder.itemView.edit_drawer)

            TransitionManager.beginDelayedTransition(holder.itemView.parent_slide, transition)
            holder.itemView.edit_drawer.setVisibility(View.VISIBLE)
        }

        holder.itemView.more_close.setOnClickListener {

            //holder.itemView.name2.setVisibility(View.VISIBLE)

            val transition: Transition = Slide(Gravity.RIGHT)
            transition.setDuration(500)
            transition.addTarget(holder.itemView.edit_drawer)

            TransitionManager.beginDelayedTransition(holder.itemView.parent_slide, transition)
            holder.itemView.edit_drawer.setVisibility(View.INVISIBLE)
        }


    }


    fun setData(task: MutableList<Tasks>){
        this.list = task
        notifyDataSetChanged()
    }


}