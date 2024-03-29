package com.top.planer.gui.pages.home.tasks

import android.app.TimePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.util.SparseIntArray
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.common.BackgroundShapeType
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.aminography.primedatepicker.picker.theme.LightThemeFactory
import com.top.planer.R
import com.top.planer.algorithm.IO
import com.top.planer.entities.Notes
import com.top.planer.entities.Tasks
import com.top.planer.entities.Types
import com.top.planer.gui.callBacks.NoteDiffCallback
import com.top.planer.gui.callBacks.TaskDiffCallback
import com.top.planer.gui.callBacks.TypeDiffCallback
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.single_task.view.*
import kotlinx.android.synthetic.main.single_task.view.task_title
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

// klasa odpowiedzialna za umieszczanie pojedynczych tasków w recyclerView
class AdapterTasks(
    val fragmentManager: FragmentManager,
    var list: MutableList<Tasks>,
    var notesList: MutableList<Notes>,
    var typesList: MutableList<Types>,
    private val deleteListener: (idTast: Int, idNote: Int) -> Unit,
    private val updateListener: (task: Tasks, note: Notes) -> Unit
): RecyclerView.Adapter<AdapterTasks.ViewHolder>() {

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
        val itemsNote = notesList.find { notes -> notes.noteId == item.noteId }
        val itemsType: Types? = typesList.find { type -> type.id == item.typeId}


        holder.itemView.task_title.text = item.title

        fun compareDates(date1: String, date2: String): Int     // d1==d2 -> 0  ; d1>d2 -> 1
        {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val dateTime1 = dateFormat.parse(date1.replace("e", ""))
            val dateTime2 = dateFormat.parse(date2.replace("e", ""))

            return dateTime1.compareTo(dateTime2)
        }

        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        if(compareDates(currentDateTime.format(formatter), item.deadline) > 0)
        {
            val dateTmp = item.deadline.substring(8, 10) + '.' +
                    item.deadline.substring(5, 7) + '.' +
                    item.deadline.substring(0, 4) +' ' +
                    item.deadline.substring(11) + "  ⚠"

            holder.itemView.task_date.text = dateTmp
        }
        else
        {
            val dateTmp = item.deadline.substring(8, 10) + '.' +
                    item.deadline.substring(5, 7) + '.' +
                    item.deadline.substring(0, 4) +' ' +
                    item.deadline.substring(11)

            holder.itemView.task_date.text = dateTmp
        }

        val drawable = holder.itemView.task_layout.background as GradientDrawable

        val color = if (itemsType != null) Color.parseColor(itemsType.colour) else ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off)
        drawable.setStroke(5, color)


        holder.itemView.done.setOnClickListener {

            var clicked = 0
            //TODO wypisac komunikat z zapytaniem
            val snackbar = Snackbar.make(holder.itemView, "Czy chcesz cofnąc usunięcie?", Snackbar.LENGTH_LONG)
            snackbar.duration = 2000
            snackbar.setAction("cofnij") {
                clicked = 1
            }

            snackbar.show()
            holder.itemView.done.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.icon_checkbox_filled))
            holder.itemView.done.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off), PorterDuff.Mode.SRC_ATOP)

            //miejsce, w którym odhaczanie zadań jest zapamiętywane
            Handler().postDelayed({
                if(clicked == 0)
                {
                    val io = IO()
                    io.updateWork(holder.itemView.context, item.timeToFinish)
                    deleteListener(item.id, item.noteId!!)
                }
                holder.itemView.done.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.icon_checkbox_empty))
                holder.itemView.done.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off), PorterDuff.Mode.SRC_ATOP)

            }, 2000) // opóźnienie wynosi 5000 milisekund, czyli 5 sekund

        }

        holder.itemView.btn_delete.setOnClickListener {
            deleteListener(item.id, item.noteId!!)

            //TODO uruchomic algo?
        }

        // edycja tasku
        holder.itemView.btn_edit.setOnClickListener {

            var title: String
            var importance: Int = item.importance
            var urgency: Int = item.urgency
            var deadline_day: String        // YYYY-MM-DD
            var deadline_time: String       // HH:MM


            var duration: Int = item.timeToFinish
            var isActive: Int = item.isActive
            var typeId: Int? = item.typeId

            var specyfic_date: Int = 0

            val builder = AlertDialog.Builder(holder.itemView.context) //TODO

            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogView = inflater.inflate(R.layout.dialog_editing_task, null)
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

            val type_spinner = dialogView.findViewById<Spinner>(R.id.type_spinner)


            val toThisDay = dialogView.findViewById<TextView>(R.id.to_this_day)
            val atThisDay = dialogView.findViewById<TextView>(R.id.at_this_day)

            val task_note = dialogView.findViewById<AppCompatEditText>(R.id.note)

            val cancel = dialogView.findViewById<Button>(R.id.btn_cancel)
            val edit = dialogView.findViewById<Button>(R.id.btn_create)
            edit.setText("Edytuj")

            //ustawianie wartosci taska
            task_title.setText(item.title)
            task_note.setText(itemsNote?.noteContent)

            when(item.importance)
            {
                0 -> { important0.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on)) }
                1 -> { important1.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on)) }
            }

            when(item.urgency)
            {
                0 -> { urgent0.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on)) }
                1 -> { urgent1.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on)) }
            }


            when(item.timeToFinish)
            {
                1 -> {
                    duration1.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                }
                2 -> {
                    duration2.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                }
                6 -> {
                    duration3.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                }
                12 -> {
                    duration4.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                }
                24 -> {
                    duration5.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                }
                30 -> {
                    duration6.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                }
            }

            var listOfTypes = mutableListOf<String>()
            listOfTypes.add("Brak")
            for(types in typesList) {
                listOfTypes.add(types.name)
            }

            val adapter = ArrayAdapter(holder.itemView.context, android.R.layout.simple_spinner_dropdown_item, listOfTypes)
            type_spinner.adapter = adapter
            type_spinner.setSelection(0)

            when(item.typeId)
            {
                0 -> {
                    type_spinner.setSelection(0)
                }
                1 -> {
                    type_spinner.setSelection(1)
                }
                2 -> {
                    type_spinner.setSelection(2)
                }
                3 -> {
                    type_spinner.setSelection(3)
                }
                4 -> {
                    type_spinner.setSelection(4)
                }
            }

            if(item.date != null)
            {
                atThisDay.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                specyfic_date = 1
            }
            else
            {
                toThisDay.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                specyfic_date = 0
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
                        important0.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }
                    1 -> {
                        important1.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }
                }

            }

            fun uncheckUrgency()
            {
                when(urgency)
                {
                    0 -> {
                        urgent0.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }
                    1 -> {
                        urgent1.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }
                }

            }


            important0.setOnClickListener {
                uncheckImportance()
                important0.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                importance = 0
            }

            important1.setOnClickListener {
                uncheckImportance()
                important1.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                importance = 1
            }

            urgent0.setOnClickListener {
                uncheckUrgency()
                urgent0.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                urgency = 0
            }

            urgent1.setOnClickListener {
                uncheckUrgency()
                urgent1.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
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

                val themeFactory = object : LightThemeFactory() {

                    override val pickedDayBackgroundShapeType: BackgroundShapeType
                        get() = BackgroundShapeType.ROUND_SQUARE

                    override val calendarViewPickedDayBackgroundColor: Int
                        get() = getColor(R.color.brown_important_urgent_on)

                    override val calendarViewWeekLabelTextColors: SparseIntArray
                        get() = SparseIntArray(7).apply {
                            val black = getColor(R.color.black)
                            put(Calendar.SATURDAY, black)
                            put(Calendar.SUNDAY, black)
                            put(Calendar.MONDAY, black)
                            put(Calendar.TUESDAY, black)
                            put(Calendar.WEDNESDAY, black)
                            put(Calendar.THURSDAY, black)
                            put(Calendar.FRIDAY, black)
                        }

                    override val calendarViewShowAdjacentMonthDays: Boolean
                        get() = true

                    override val selectionBarBackgroundColor: Int
                        get() = getColor(R.color.brown_important_urgent_on)

                    override val selectionBarRangeDaysItemBackgroundColor: Int
                        get() = getColor(R.color.brown_important_urgent_off)
                }

                val primeCalendar = CivilCalendar(TimeZone.getDefault(), Locale("pl", "PL"))

                val callback = SingleDayPickCallback { day ->
                    val localDate: LocalDate = day.getTime()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    setDateBlocks(localDate.toString())
                    deadline_day= setUpDate(localDate.dayOfMonth,localDate.monthValue-1,localDate.year)
                }

                val datePicker = PrimeDatePicker.dialogWith(primeCalendar)
                    .pickSingleDay(callback)
                    .initiallyPickedSingleDay(primeCalendar)
                    .minPossibleDate(primeCalendar)
                    .firstDayOfWeek(Calendar.MONDAY)
                    .applyTheme(themeFactory)
                    .build()

                datePicker.show(fragmentManager, "AddingTaskDatePicker")

            }

            clock.setOnClickListener {

                val cal = Calendar.getInstance()
                val today_hour = cal.get(Calendar.HOUR)
                val today_minute = cal.get(Calendar.MINUTE)

                var tpd = TimePickerDialog(
                    holder.itemView.context,
                    R.style.MyDatePickerStyle,
                    TimePickerDialog.OnTimeSetListener { view, sel_hour, sel_minutes ->

                        deadline_time = setUpTime(sel_hour, sel_minutes)
                        setTimeBlocks(deadline_time)

                    }, today_hour, today_minute,
                    true
                )
                tpd.show()
            }

            toThisDay.setOnClickListener{
                atThisDay.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.pr1_this_day_off))
                toThisDay.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                specyfic_date = 0
            }

            atThisDay.setOnClickListener{
                toThisDay.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.pr1_this_day_off))
                atThisDay.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                specyfic_date = 1
            }

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
                    6 -> {
                        duration3.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }
                    12 -> {
                        duration4.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }
                    24 -> {
                        duration5.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }
                    30 -> {
                        duration6.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }

                }

            }

            duration1.setOnClickListener {
                uncheckDuration()

                    //binding.duration2.setColorFilter(getResources().getColor(R.color.hard_red));
                    duration1.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                    duration = 1

            }

            duration2.setOnClickListener {
                uncheckDuration()
                if(duration == 2)
                {
                    //binding.duration2.setColorFilter(getResources().getColor(R.color.hard_red));
                    duration1.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                    duration = 1
                }
                else{
                    duration2.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                    duration = 2
                }
            }

            duration3.setOnClickListener {
                uncheckDuration()
                if(duration == 6)
                {
                    //binding.duration2.setColorFilter(getResources().getColor(R.color.hard_red));
                    duration1.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                    duration = 1
                }
                else{
                    duration3.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                    duration = 6
                }
            }

            duration4.setOnClickListener {
                uncheckDuration()
                if(duration == 12)
                {
                    //binding.duration2.setColorFilter(getResources().getColor(R.color.hard_red));
                    duration1.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                    duration = 1
                }
                else{
                    duration4.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                    duration = 12
                }
            }

            duration5.setOnClickListener {
                uncheckDuration()
                if(duration == 24)
                {
                    //binding.duration2.setColorFilter(getResources().getColor(R.color.hard_red));
                    duration1.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                    duration = 1
                }
                else{
                    duration5.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                    duration = 24
                }
            }

            duration6.setOnClickListener {
                uncheckDuration()
                if(duration == 30)
                {
                    //binding.duration2.setColorFilter(getResources().getColor(R.color.hard_red));
                    duration1.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                    duration = 1
                }
                else{
                    duration6.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                    duration = 30
                }
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
                    type_spinner.selectedItemPosition,
                    item.noteId,
                    date = if (specyfic_date == 1) "$deadline_day $deadline_time" else null
                )

                updateListener(task, Notes(item.noteId!!, task_title.text.toString(), task_note.text.toString(), null))
                alertDialog.cancel()

            }

        }

        // wyswietlanie info o tasku
        holder.itemView.setOnClickListener{
            val builder = AlertDialog.Builder(holder.itemView.context) //TODO

            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogView = inflater.inflate(R.layout.dialog_task_info, null)
            builder.setView(dialogView)

            val title = dialogView.findViewById<TextView>(R.id.title)
            val date = dialogView.findViewById<TextView>(R.id.date)
            val type = dialogView.findViewById<TextView>(R.id.type)
            val type_cont = dialogView.findViewById<TextView>(R.id.type_content)
            val note_cont = dialogView.findViewById<TextView>(R.id.note_content)
            val note = dialogView.findViewById<TextView>(R.id.note)

            builder.setView(dialogView) //Podlaczanie xmla

            title.setText(item.title)
            title.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.pr1_green_text))

            val dateTmp = item.deadline.substring(8, 10) + '.' +
                    item.deadline.substring(5, 7) + '.' +
                    item.deadline.substring(0, 4) +' ' +
                    item.deadline.substring(11)

            date.setText(dateTmp)

            if(itemsType != null)
            {
                type.visibility = View.VISIBLE
                type_cont.visibility = View.VISIBLE
                type_cont.setText(itemsType.name)
            }
            else
            {
                type.visibility = View.GONE
                type_cont.visibility = View.GONE
            }

            if(itemsNote?.noteContent?.replace(" ", "") != "")
            {
                note.visibility = View.VISIBLE
                note_cont.visibility = View.VISIBLE
                note_cont.setText(itemsNote?.noteContent)
            }
            else
            {
                note.visibility = View.GONE
                note_cont.visibility = View.GONE
            }

            val alertDialog = builder.create()
            alertDialog.show()

        }

        holder.itemView.edit_drawer.visibility = View.GONE

        holder.itemView.more_open.setOnClickListener {

            val transition: Transition = Slide(Gravity.RIGHT)
            transition.setDuration(500)
            transition.addTarget(holder.itemView.edit_drawer)

            TransitionManager.beginDelayedTransition(holder.itemView.parent_slide, transition)
            holder.itemView.edit_drawer.setVisibility(View.VISIBLE)
        }

        holder.itemView.more_close.setOnClickListener {

            val transition: Transition = Slide(Gravity.RIGHT)
            transition.setDuration(500)
            transition.addTarget(holder.itemView.edit_drawer)

            TransitionManager.beginDelayedTransition(holder.itemView.parent_slide, transition)
            holder.itemView.edit_drawer.setVisibility(View.INVISIBLE)
        }

    }
    fun updateList(newTask: MutableList<Tasks>) {
        val diffResult = DiffUtil.calculateDiff(
            TaskDiffCallback(this.list, newTask)
        )
        this.list = newTask

        diffResult.dispatchUpdatesTo(this)
    }

    fun updateListOfNotes(newNote: MutableList<Notes>) {
        val diffResult = DiffUtil.calculateDiff(
            NoteDiffCallback(this.notesList, newNote)
        )
        this.notesList = newNote
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateListOfTypes(newTypes: MutableList<Types>) {
        val diffResult = DiffUtil.calculateDiff(
            TypeDiffCallback(this.typesList, newTypes)
        )
        this.typesList = newTypes
        diffResult.dispatchUpdatesTo(this)
    }

}