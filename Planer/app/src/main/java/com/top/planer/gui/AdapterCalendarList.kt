package com.top.planer.gui


import android.app.TimePickerDialog
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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
import com.top.planer.entities.Calendar
import com.top.planer.entities.Notes
import com.top.planer.gui.callBacks.NoteDiffCallback
import kotlinx.android.synthetic.main.dialod_when_title_empty.view.*
import kotlinx.android.synthetic.main.single_event.view.*

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class AdapterCalendarList (
    val fragmentManager: FragmentManager,
    var list: MutableList<Calendar>,
    var list2: MutableList<Calendar>,
    var notesList: MutableList<Notes>,
    private val updateListener: (calendar:Calendar,note: Notes) -> Unit,
    private val deleteListener: (id: Long, typeDelId: Int, idNote: Int) -> Unit,
) : RecyclerView.Adapter<AdapterCalendarList.ViewHolder>() {

    class ViewHolder(itemView: CardView) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_event, parent, false) as CardView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val inflater = LayoutInflater.from(holder.itemView.context)
        val dialogViewDeleteSeries = inflater.inflate(R.layout.dialog_delete_event_series, null)
        val buttonSingleEventDelete = dialogViewDeleteSeries.findViewById<Button>(R.id.delete_single)
        val buttonSeriesEventDelete = dialogViewDeleteSeries.findViewById<Button>(R.id.delete_series)

        val builder = AlertDialog.Builder(holder.itemView.context)


        val primeCalendar = CivilCalendar(TimeZone.getDefault(), Locale("pl", "PL"))

        val item = list[position]
        holder.itemView.title_event.text = item.name
        holder.itemView.date_event.text = item.startDate.toString().substring(10,16)
        val itemsNote = notesList.find { notes -> notes.noteId == item.noteId }

        holder.itemView.btn_delete_event.setOnClickListener {
            if (item.typeId!=0)
            {
                builder.setView(dialogViewDeleteSeries)
                val alertDialog = builder.create()
                alertDialog.show()

                buttonSeriesEventDelete.setOnClickListener {
                    deleteListener(item.id, item.typeId, item.noteId)
                    alertDialog.hide()

                }


                buttonSingleEventDelete.setOnClickListener {
                    deleteListener(item.id, 0, item.noteId)
                    alertDialog.hide()

                }


            }
            else
            {
                deleteListener(item.id, item.typeId, item.noteId)
            }

        }


        holder.itemView.btn_edit_event.setOnClickListener{

            var startdate_year_month_day: String
            var startdate_time: String

            var enddate_year_month_day: String
            var enddate_time: String

            var repeat: Int = item.repeatId
            var remind: Int = item.reminder

            var typeId: Int = item.typeId
            var noteId: Int = item.noteId

            var locationEdited: String = item.location

            val builder = AlertDialog.Builder(holder.itemView.context)
            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogView = inflater.inflate(R.layout.dialog_edit_event_new, null)
            builder.setView(dialogView)

            val remind1 = dialogView.findViewById<TextView>(R.id.tv_reminder_15min)
            val remind2 = dialogView.findViewById<TextView>(R.id.tv_remind_1h)
            val remind3 = dialogView.findViewById<TextView>(R.id.tv_remind_day)

            val titleEvent = dialogView.findViewById<AppCompatEditText>(R.id.event_title)

            val dayBegin = dialogView.findViewById<TextView>(R.id.tv_begin_event_day)
            val monthBegin = dialogView.findViewById<TextView>(R.id.tv_begin_event_month)
            val yearBegin = dialogView.findViewById<TextView>(R.id.tv_begin_event_year)
            val hourBegin = dialogView.findViewById<TextView>(R.id.tv_begin_event_hour)
            val minutesBegin = dialogView.findViewById<TextView>(R.id.tv_begin_event_min)

            val dayEnd = dialogView.findViewById<TextView>(R.id.tv_end_event_day)
            val monthEnd = dialogView.findViewById<TextView>(R.id.tv_end_event_month)
            val yearEnd = dialogView.findViewById<TextView>(R.id.tv_end_event_year)
            val hourEnd = dialogView.findViewById<TextView>(R.id.tv_end_event_hour)
            val minutesEnd = dialogView.findViewById<TextView>(R.id.tv_end_event_min)

            val calendarBegin = dialogView.findViewById<ImageView>(R.id.btn_begin_event)
            val clockBegin = dialogView.findViewById<ImageView>(R.id.btn_begin_event_time)

            val calendarEnd = dialogView.findViewById<ImageView>(R.id.btn_end_event)
            val clockEnd = dialogView.findViewById<ImageView>(R.id.btn_end_event_time)

            val location = dialogView.findViewById<EditText>(R.id.tv_location)
            val noteString = dialogView.findViewById<EditText>(R.id.tv_note)

            val cancelEdit = dialogView.findViewById<Button>(R.id.btn_cancel_edit_event)
            val doneEdit = dialogView.findViewById<Button>(R.id.btn_done_edit_event)

            titleEvent.setText(item.name)
            noteString.setText(itemsNote?.noteContent)

            location.setText(item.location)

            when(item.reminder) {
                1 -> remind1.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                2 -> remind2.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
                3 -> remind3.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))
            }

            val alertDialog = builder.create()
            alertDialog.show()

            cancelEdit.setOnClickListener {
                alertDialog.hide()
            }

            startdate_year_month_day = item.startDate.substring(0,10)

            startdate_time = item.startDate.substring(11)

            val datetoFormat = startdate_year_month_day.split('-')
            val timetoFormat = startdate_time.split(':')

            enddate_year_month_day = item.endDate.substring(0,10)
            enddate_time = item.endDate.substring(11)

            val datetoFormatEnd = enddate_year_month_day.split('-')
            val timetoFormatEnd = enddate_time.split(':')

            yearBegin.setText(datetoFormat[0])
            monthBegin.setText(datetoFormat[1])
            dayBegin.setText(datetoFormat[2])

            hourBegin.setText(timetoFormat[0])
            minutesBegin.setText(timetoFormat[1])

            yearEnd.setText(datetoFormatEnd[0])
            monthEnd.setText(datetoFormatEnd[1])
            dayEnd.setText(datetoFormatEnd[2])

            hourEnd.setText(timetoFormatEnd[0])
            minutesEnd.setText(timetoFormatEnd[1])

            fun setDateBlocks(date: String) {
                val table = date.split('-')
                dayBegin.setText(table[2])
                monthBegin.setText(table[1])
                yearBegin.setText(table[0])
            }
            fun setEndDateBlocks(date: String) {
                val table = date.split('-')
                dayEnd.setText(table[2])
                monthEnd.setText(table[1])
                yearEnd.setText(table[0])
            }

            fun setUpDate(d: Int, m: Int, y: Int): String {
                var month = m.toString()
                var day = d.toString()

                month.takeIf { m + 1 < 10 }?.let { month = "0" + (m + 1) }
                day.takeIf { d < 10 }?.let { day = "0" + d }

                return "$y-$month-$day"
            }

            fun setUpTime(h: Int, m: Int): String {
                var hour = h.toString()
                var minutes = m.toString()

                hour.takeIf { h < 10 }?.let { hour = "0" + (h) }
                minutes.takeIf { m < 10 }?.let { minutes = "0" + m }

                return "$hour:$minutes"
            }

            fun setTimeBlocks(date: String) {
                val table = date.split(':')
                hourBegin.setText(table[0])
                minutesBegin.setText(table[1])
            }

            fun setEndTimeBlocks(date: String) {
                val table = date.split(':')
                hourEnd.setText(table[0])
                minutesEnd.setText(table[1])
            }

            calendarBegin.setOnClickListener {

                val themeFactory = object : LightThemeFactory() {

                    override val pickedDayBackgroundShapeType: BackgroundShapeType
                        get() = BackgroundShapeType.ROUND_SQUARE

                    override val calendarViewPickedDayBackgroundColor: Int
                        get() = getColor(R.color.brown_important_urgent_on)


                    override val calendarViewWeekLabelTextColors: SparseIntArray
                        get() = SparseIntArray(7).apply {
                            val black = getColor(R.color.black)
                            put(java.util.Calendar.SATURDAY, black)
                            put(java.util.Calendar.SUNDAY, black)
                            put(java.util.Calendar.MONDAY, black)
                            put(java.util.Calendar.TUESDAY, black)
                            put(java.util.Calendar.WEDNESDAY, black)
                            put(java.util.Calendar.THURSDAY, black)
                            put(java.util.Calendar.FRIDAY, black)
                        }

                    override val calendarViewShowAdjacentMonthDays: Boolean
                        get() = true

                    override val selectionBarBackgroundColor: Int
                        get() = getColor(R.color.brown_important_urgent_on)

                    override val selectionBarRangeDaysItemBackgroundColor: Int
                        get() = getColor(R.color.brown_important_urgent_off)


                }

                val callback = SingleDayPickCallback { day ->
                    val localDate: LocalDate = day.getTime()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    setDateBlocks(localDate.toString())
                    startdate_year_month_day= setUpDate(localDate.dayOfMonth,localDate.monthValue-1,localDate.year)
                }

                val datePicker = PrimeDatePicker.dialogWith(primeCalendar)
                    .pickSingleDay(callback)
                    .initiallyPickedSingleDay(primeCalendar)
                    .minPossibleDate(primeCalendar)
                    .firstDayOfWeek(java.util.Calendar.MONDAY)
                    .applyTheme(themeFactory)
                    .build()

                datePicker.show(fragmentManager, "AddingTaskDatePicker")
            }

            calendarEnd.setOnClickListener {


                val themeFactory = object : LightThemeFactory() {

                    override val pickedDayBackgroundShapeType: BackgroundShapeType
                        get() = BackgroundShapeType.ROUND_SQUARE

                    override val calendarViewPickedDayBackgroundColor: Int
                        get() = getColor(R.color.brown_important_urgent_on)


                    override val calendarViewWeekLabelTextColors: SparseIntArray
                        get() = SparseIntArray(7).apply {
                            val black = getColor(R.color.black)
                            put(java.util.Calendar.SATURDAY, black)
                            put(java.util.Calendar.SUNDAY, black)
                            put(java.util.Calendar.MONDAY, black)
                            put(java.util.Calendar.TUESDAY, black)
                            put(java.util.Calendar.WEDNESDAY, black)
                            put(java.util.Calendar.THURSDAY, black)
                            put(java.util.Calendar.FRIDAY, black)
                        }

                    override val calendarViewShowAdjacentMonthDays: Boolean
                        get() = true

                    override val selectionBarBackgroundColor: Int
                        get() = getColor(R.color.brown_important_urgent_on)

                    override val selectionBarRangeDaysItemBackgroundColor: Int
                        get() = getColor(R.color.brown_important_urgent_off)


                }

                val callback = SingleDayPickCallback { day ->
                    val localDate: LocalDate = day.getTime()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    setEndDateBlocks(localDate.toString())
                    enddate_year_month_day= setUpDate(localDate.dayOfMonth,localDate.monthValue-1,localDate.year)
                }

                val datePicker = PrimeDatePicker.dialogWith(primeCalendar)
                    .pickSingleDay(callback)
                    .initiallyPickedSingleDay(primeCalendar)
                    .minPossibleDate(primeCalendar)
                    .firstDayOfWeek(java.util.Calendar.MONDAY)
                    .applyTheme(themeFactory)
                    .build()

                datePicker.show(fragmentManager, "AddingTaskDatePicker")
            }

            clockBegin.setOnClickListener {


                var tpd = TimePickerDialog(
                    holder.itemView.context,
                    R.style.MyDatePickerStyle,
                    TimePickerDialog.OnTimeSetListener { view, sel_hour, sel_minutes ->

                        startdate_time = setUpTime(sel_hour, sel_minutes)
                        setTimeBlocks(startdate_time)

                    }, hourBegin.text.toString().toInt(), minutesBegin.text.toString().toInt(),
                    true
                )
                tpd.show()
            }

            clockEnd.setOnClickListener {


                var tpd = TimePickerDialog(
                    holder.itemView.context,
                    R.style.MyDatePickerStyle,
                    TimePickerDialog.OnTimeSetListener { view, sel_hour, sel_minutes ->

                        enddate_time = setUpTime(sel_hour, sel_minutes)
                        setEndTimeBlocks(enddate_time)

                    }, hourEnd.text.toString().toInt(), minutesEnd.text.toString().toInt(),
                    true
                )
                tpd.show()
            }

            startdate_year_month_day = yearBegin.text.toString() + '-'+
                    monthBegin.text.toString() + '-' +
                    dayBegin.text.toString()


            startdate_time =
                hourBegin.text.toString() + ':' +
                        minutesBegin.text.toString()

            fun resetRemind() {
                when(remind)
                {

                    1->
                    {
                        remind1.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }

                    2->
                    {
                        remind2.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }

                    3->
                    {
                        remind3.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_off))
                    }
                }
            }

            remind1.setOnClickListener {
                resetRemind()
                if (remind == 1) // kliknelismy drugi raz w to samo pole
                {
                    remind = 0
                }
                else
                {
                    remind = 1
                    remind1.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))

                }

            }

            remind2.setOnClickListener {
                resetRemind()
                if (remind == 2) // kliknelismy drugi raz w to samo pole
                {
                    remind = 0
                }
                else
                {
                    remind = 2
                    remind2.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))

                }
            }

            remind3.setOnClickListener {
                resetRemind()
                if (remind == 3) // kliknelismy drugi raz w to samo pole
                {
                    remind = 0
                }
                else
                {
                    remind = 3
                    remind3.getBackground().setTint(ContextCompat.getColor(holder.itemView.context, R.color.brown_important_urgent_on))

                }
            }

            fun compareDates(date1: String, date2: String): Int {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val dateTime1 = dateFormat.parse(date1.replace("e", ""))
                val dateTime2 = dateFormat.parse(date2.replace("e", ""))

                return dateTime1.compareTo(dateTime2)
            }

            fun addOneHour(dateString: String): String {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
                val date = dateFormat.parse(dateString)
                val calendar = java.util.Calendar.getInstance()
                calendar.time = date
                calendar.add(java.util.Calendar.HOUR_OF_DAY, 1)
                return dateFormat.format(calendar.time)
            }

            doneEdit.setOnClickListener {

                //porównuje czy data koncowa jest pozniejsza niz poczatkowa

                val date1 = startdate_year_month_day +' '+startdate_time
                val date2 = enddate_year_month_day + ' '+enddate_time
                val result = compareDates(date1, date2)

                if (result > 0 ) //jeżeli data koncowa jest wczesniej niz poczatkowa, to ustawiam ja na godzine pozniej niz poczatkowa
                {
                    val newDate = addOneHour(date1)
                    enddate_year_month_day = newDate.substring(0,10)
                    enddate_time = newDate.substring(11,16)
                }

                locationEdited = location.text.toString()

                if(titleEvent.text.toString().replace(" ", "") == "")
                {
                    val builder = AlertDialog.Builder(holder.itemView.context)
                    val inflater = LayoutInflater.from(holder.itemView.context)
                    val dialogView = inflater.inflate(R.layout.dialod_when_title_empty, null)
                    builder.setView(dialogView) //Podlaczanie xmla
                    val alertDialog = builder.create()
                    alertDialog.show()

                    dialogView.btn_ok.setOnClickListener {
                        alertDialog.cancel()
                    }

                }
                else {


                    var event = Calendar(
                        item.id,
                        startdate_year_month_day + ' ' + startdate_time,
                        enddate_year_month_day + ' ' + enddate_time,
                        typeId,
                        remind,
                        location.text.toString(),
                        repeat,
                        item.noteId,
                        titleEvent.text.toString()
                    )


                    updateListener(
                        event,
                        Notes(
                            item.noteId,
                            titleEvent.text.toString(),
                            noteString.text.toString(),
                            null
                        )
                    )

                    alertDialog.cancel()
                }
            }
        }

        holder.itemView.setOnClickListener{

            val builder = AlertDialog.Builder(holder.itemView.context) //TODO

            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogView = inflater.inflate(R.layout.dialog_event_info, null)
            builder.setView(dialogView)

            val title = dialogView.findViewById<TextView>(R.id.title)
            val date = dialogView.findViewById<TextView>(R.id.date)
            val location = dialogView.findViewById<TextView>(R.id.location)
            val location_content = dialogView.findViewById<TextView>(R.id.location_content)
            val note_cont = dialogView.findViewById<TextView>(R.id.note_content)
            val note = dialogView.findViewById<TextView>(R.id.note)

            builder.setView(dialogView)

            title.setText(item.name)

            val dateTmp = item.startDate.substring(8, 10) + '.' +
                    item.startDate.substring(5, 7) + '.' +
                    item.startDate.substring(0, 4) +' ' +
                    item.startDate.substring(11)

            date.setText(dateTmp)

            if(!item.location.isEmpty())
            {
                location_content.text = item.location
            }
            else
            {
                location.visibility = View.GONE
                location_content.visibility=View.GONE
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
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newCalendar: MutableList<Calendar>) {
        val diffResult = DiffUtil.calculateDiff(
            com.top.planer.gui.callBacks.CalendarDiffCallback(
                this.list,
                newCalendar
            )
        )
        this.list = newCalendar
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateList2(newCalendar: MutableList<Calendar>) {
        val diffResult = DiffUtil.calculateDiff(
            com.top.planer.gui.callBacks.CalendarDiffCallback(
                this.list2,
                newCalendar
            )
        )
        this.list2 = newCalendar
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateListOfNotes(newNote: MutableList<Notes>) {
        val diffResult = DiffUtil.calculateDiff(
            NoteDiffCallback(this.notesList, newNote)
        )
        this.notesList = newNote
        diffResult.dispatchUpdatesTo(this)
    }
}

