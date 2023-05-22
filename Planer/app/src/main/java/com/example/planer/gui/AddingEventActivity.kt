package com.example.planer.gui

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.SparseIntArray
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.common.BackgroundShapeType
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.aminography.primedatepicker.picker.theme.LightThemeFactory
import com.example.planer.R
import com.example.planer.ViewModel.CalendarViewModel
import com.example.planer.databinding.AddEventToCalendarBinding
import com.example.planer.entities.Notes
import kotlinx.android.synthetic.main.dialod_when_title_empty.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class   AddingEventActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: AddEventToCalendarBinding
    private lateinit var calendarViewModel: CalendarViewModel

    private val primeCalendar = CivilCalendar(TimeZone.getDefault(), Locale("pl", "PL"))
    // zmienne do stworzenia nowego eventu

    var startDate : String = ""
    var startTime : String = ""
    var endTime : String = ""
    var endDate : String = ""
    var reminder: Int = 0 //0 - brak   1 - 15 min   2 - godzine przed   3 - dzień przed
    var repeat : Int = 0 // 0 - brak   1 - tydzień  2 - miesiąc         3 - rok
    var noteId: Int = 0

    //calendar - pobranie aktualnego czasu
    val calendar = Calendar.getInstance()
    var today_year = calendar.get(Calendar.YEAR)
    var today_month = calendar.get(Calendar.MONTH)
    var today_day = calendar.get(Calendar.DAY_OF_MONTH)
    var today_hour = calendar.get(Calendar.HOUR_OF_DAY)
    var today_minute = calendar.get((Calendar.MINUTE))

    override fun onCreate(savedInstanceState: Bundle?) {

        val selectedDateMillis = intent.getLongExtra("selected_date", 0L) // 0L is the default value if the key is not found
        val selectedDate = Date(selectedDateMillis) // create a Date object from the selected date in milliseconds
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val selectedDateString = dateFormat.format(selectedDate)

        super.onCreate(savedInstanceState)

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        binding = AddEventToCalendarBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //przyciski
        binding.btnCancel.setOnClickListener(this)
        binding.btnCreate.setOnClickListener(this)

        //pola tekstowe - tytuł, daty i notatka i lokalizacja
        binding.eventTitle.setOnClickListener(this)

        binding.tvBeginEventDay.setOnClickListener(this)
        binding.tvBeginEventMonth.setOnClickListener(this)
        binding.tvBeginEventYear.setOnClickListener(this)
        binding.tvBeginEventHour.setOnClickListener(this)
        binding.tvBeginEventMin.setOnClickListener(this)

        binding.tvEndEventDay.setOnClickListener(this)
        binding.tvEndEventMonth.setOnClickListener(this)
        binding.tvEndEventYear.setOnClickListener(this)
        binding.tvEndEventHour.setOnClickListener(this)
        binding.tvEndEventMin.setOnClickListener(this)

        binding.tvRepeatWeek.setOnClickListener(this)
        binding.tvRepeatMonth.setOnClickListener(this)
        binding.tvRepeatYear.setOnClickListener(this)

        binding.tvReminder15min.setOnClickListener(this)
        binding.tvRemind1h.setOnClickListener(this)
        binding.tvRemindDay.setOnClickListener(this)

        binding.tvNote.setOnClickListener(this)

        binding.tvLocation.setOnClickListener(this)


        binding.btnBeginEvent.setOnClickListener(this)
        binding.btnBeginEventTime.setOnClickListener(this)

        binding.btnEndEvent.setOnClickListener(this)
        binding.btnEndEventTime.setOnClickListener(this)

        today_day = selectedDateString.substring(8).toInt()
        today_month = selectedDateString.substring(5, 7).toInt()
        today_year= selectedDateString.substring(0, 4).toInt()

        setInitialDateBlocks(setUpDate(today_day, today_month, today_year))
        setInitialTimeBlocks(setUpTime(today_hour, today_minute))
    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.tv_repeat_week->
            {
                resetRepeat()

                if (repeat == 1) // kliknelismy drugi raz w to samo pole
                {
                    repeat = 0
                    return
                }

                repeat = 1
                binding.tvRepeatWeek.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_on)))

            }

            R.id.tv_repeat_month->
            {
                resetRepeat()

                if (repeat == 2) // kliknelismy drugi raz w to samo pole
                {
                    repeat = 0
                    return
                }

                repeat = 2
                binding.tvRepeatMonth.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_on)))

            }

            R.id.tv_repeat_year->
            {
                resetRepeat()

                if (repeat == 3) // kliknelismy drugi raz w to samo pole
                {
                    repeat = 0
                    return
                }

                repeat = 3
                binding.tvRepeatYear.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_on)))

            }

            R.id.tv_reminder_15min->
            {
                resetRemind()

                if (reminder == 1) // kliknelismy drugi raz w to samo pole
                {
                    reminder = 0
                    return
                }

                reminder = 1
                binding.tvReminder15min.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_on)))

            }

            R.id.tv_remind_1h->
            {
                resetRemind()

                if (reminder == 2) // kliknelismy drugi raz w to samo pole
                {
                    reminder = 0
                    return
                }

                reminder = 2
                binding.tvRemind1h.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_on)))

            }

            R.id.tv_remind_day->
            {
                resetRemind()

                if (reminder == 3) // kliknelismy drugi raz w to samo pole
                {
                    reminder = 0
                    return
                }

                reminder = 3
                binding.tvRemindDay.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_on)))

            }

            R.id.btn_create->{

                var dayb: String = binding.tvBeginEventDay.text.toString()
                if (dayb == "") {
                    dayb = today_day.toString()
                }

                var monthb: String = binding.tvBeginEventMonth.text.toString()
                if (monthb == "") {
                    monthb = today_month.toString()
                }

                var yearb: String = binding.tvBeginEventYear.text.toString()
                if (yearb == "") {
                    yearb = today_year.toString()
                }

                var hourb: String = binding.tvBeginEventHour.text.toString()
                if (hourb == "") {
                    hourb = today_hour.toString()
                }

                var minuteb: String = binding.tvBeginEventMin.text.toString()
                if (minuteb == "") {
                    minuteb = today_minute.toString()
                }


                var daye: String = binding.tvEndEventDay.text.toString()
                if (daye == "") {
                    daye = today_day.toString()
                }       //????????????????? komunikat ze puste?

                var monthe: String = binding.tvEndEventMonth.text.toString()
                if (monthe == "") {
                    monthe = today_month.toString()
                }

                var yeare: String = binding.tvEndEventYear.text.toString()
                if (yeare == "") {
                    yeare = today_year.toString()
                }

                var houre: String = binding.tvEndEventHour.text.toString()
                if (houre == "") {
                    houre = today_hour.toString()
                }

                var minutee: String = binding.tvEndEventMin.text.toString()
                if (minutee == "") {
                    minutee = today_minute.toString()
                }

                //porównuje czy data koncowa jest pozniejsza niz poczatkowa

                val date1 = "${yearb}-${monthb}-${dayb} ${hourb}:${minuteb}"
                val date2 = "${yeare}-${monthe}-${daye} ${houre}:${minutee}"
                val result = compareDates(date1, date2)

                if (result > 0 ) //jeżeli data koncowa jest wczesniej niz poczatkowa, to ustawiam ja na godzine pozniej niz poczatkowa
                {
                    val newDate = addOneHour(date1)
                    yeare = newDate.substring(0, 4)
                    monthe = newDate.substring(5, 7)
                    daye = newDate.substring(8, 10)
                    houre = newDate.substring(11, 13)
                    minutee = newDate.substring(14, 16)


                }

                if(binding.eventTitle.text.toString().replace(" ", "") == "")
                {
                    val builder = AlertDialog.Builder(this)
                    val inflater = LayoutInflater.from(this)
                    val dialogView = inflater.inflate(R.layout.dialod_when_title_empty, null)
                    builder.setView(dialogView) //Podlaczanie xmla
                    val alertDialog = builder.create()
                    alertDialog.show()

                    dialogView.btn_ok.setOnClickListener {
                        alertDialog.cancel()
                    }

                }
                else {


                    calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)

                    val initialEvent = com.example.planer.entities.Calendar(
                        startDate = setUpDate(
                            dayb.toInt(),
                            monthb.toInt(),
                            yearb.toInt()
                        ) + " " + setUpTime(hourb.toInt(), minuteb.toInt()),
                        endDate = setUpDate(
                            daye.toInt(),
                            monthe.toInt(),
                            yeare.toInt()
                        ) + " " + setUpTime(houre.toInt(), minutee.toInt()),
                        typeId = 0,
                        reminder = reminder,
                        location = binding.tvLocation.text.toString(),
                        repeatId = repeat,
                        noteId = 0,
                        name = binding.eventTitle.text.toString()
                    )

                    val note = Notes(
                        noteTitle = binding.eventTitle.text.toString(),
                        noteContent = binding.tvNote.text.toString(),
                        photo = null
                    )

                    calendarViewModel.insertEventWithNoteAndGetId(initialEvent, note) { eventId ->
                        if (repeat == 1) {
                            for (i in 1..51) {
                                val newDate = java.util.Calendar.getInstance()
                                newDate.set(yearb.toInt(), monthb.toInt() - 1, dayb.toInt())
                                newDate.add(Calendar.WEEK_OF_YEAR, 1 * i)

                                val newDateEnd = java.util.Calendar.getInstance()
                                newDateEnd.set(
                                    yeare.toInt(),
                                    monthe.toInt() - 1,
                                    daye.toInt(),
                                    houre.toInt(),
                                    minutee.toInt()
                                )
                                newDateEnd.add(Calendar.WEEK_OF_YEAR, 1 * i)

                                val note = Notes(
                                    noteTitle = binding.eventTitle.text.toString(),
                                    noteContent = binding.tvNote.text.toString(),
                                    photo = null
                                )
                                calendarViewModel.insertEventWithNote(
                                    com.example.planer.entities.Calendar(
                                        startDate = setUpDate(
                                            newDate.get(Calendar.DATE),
                                            newDate.get(Calendar.MONTH) + 1,
                                            newDate.get(Calendar.YEAR)
                                        )
                                                + " " + setUpTime(hourb.toInt(), minuteb.toInt()),
                                        endDate = setUpDate(
                                            newDateEnd.get(Calendar.DATE),
                                            newDateEnd.get(Calendar.MONTH) + 1,
                                            newDateEnd.get(Calendar.YEAR)
                                        )
                                                + " " + setUpTime(houre.toInt(), minutee.toInt()),
                                        typeId = eventId.toInt(),
                                        reminder = reminder,
                                        location = binding.tvLocation.text.toString(),
                                        repeatId = 0,
                                        noteId = 0,
                                        name = binding.eventTitle.text.toString()
                                    ),
                                    note
                                )
                            }
                        } else if (repeat == 2) //na razie powtarzanie przez rok
                        {
                            for (i in 1..11) {

                                var newDate = java.util.Calendar.getInstance()
                                newDate.set(yearb.toInt(), monthb.toInt() - 1, dayb.toInt())
                                newDate.add(Calendar.MONTH, 1 * i)

                                var newDateEnd = java.util.Calendar.getInstance()
                                newDateEnd.set(
                                    yeare.toInt(),
                                    monthe.toInt() - 1,
                                    daye.toInt(),
                                    houre.toInt(),
                                    minutee.toInt()
                                )
                                newDateEnd.add(Calendar.MONTH, 1 * i)



                                calendarViewModel.insertEventWithNote(
                                    com.example.planer.entities.Calendar(
                                        startDate = setUpDate(
                                            newDate.get(Calendar.DATE),
                                            newDate.get(Calendar.MONTH) + 1,
                                            newDate.get(Calendar.YEAR)
                                        ) + " " + setUpTime(hourb.toInt(), minuteb.toInt()),

                                        endDate = setUpDate(
                                            newDateEnd.get(Calendar.DATE),
                                            newDateEnd.get(Calendar.MONTH) + 1,
                                            newDateEnd.get(Calendar.YEAR)
                                        ) + " " + setUpTime(houre.toInt(), minutee.toInt()),
                                        typeId = eventId.toInt(),
                                        reminder = reminder,
                                        location = binding.tvLocation.text.toString(),
                                        repeatId = 0,
                                        noteId = 0,
                                        name = binding.eventTitle.text.toString()


                                    ),
                                    note
                                )


                            }

                        } else if (repeat == 3) //na razie powtarzanie przez 5 lat
                        {
                            for (i in 1..5) {

                                var newDate = java.util.Calendar.getInstance()
                                newDate.set(yearb.toInt(), monthb.toInt() - 1, dayb.toInt())
                                newDate.add(Calendar.YEAR, 1 * i)

                                var newDateEnd = java.util.Calendar.getInstance()
                                newDateEnd.set(
                                    yeare.toInt(),
                                    monthe.toInt() - 1,
                                    daye.toInt(),
                                    houre.toInt(),
                                    minutee.toInt()
                                )
                                newDateEnd.add(Calendar.YEAR, 1 * i)


                                calendarViewModel.insertEventWithNote(
                                    com.example.planer.entities.Calendar(
                                        startDate = setUpDate(
                                            newDate.get(Calendar.DATE),
                                            newDate.get(Calendar.MONTH) + 1,
                                            newDate.get(Calendar.YEAR)
                                        ) + " " + setUpTime(hourb.toInt(), minuteb.toInt()),

                                        endDate = setUpDate(
                                            newDateEnd.get(Calendar.DATE),
                                            newDateEnd.get(Calendar.MONTH) + 1,
                                            newDateEnd.get(Calendar.YEAR)
                                        ) + " " + setUpTime(houre.toInt(), minutee.toInt()),
                                        typeId = eventId.toInt(),
                                        reminder = reminder,
                                        location = binding.tvLocation.text.toString(),
                                        repeatId = 0,
                                        noteId = 0,
                                        name = binding.eventTitle.text.toString()


                                    ),
                                    note
                                )


                            }

                        }
                    }

                    finish()
                }

            }



            R.id.btn_begin_event -> {
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

                val callback = SingleDayPickCallback { day ->
                    val localDate: LocalDate = day.getTime()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    setDateBlocks(localDate.toString())
                }

                val datePicker = PrimeDatePicker.dialogWith(primeCalendar)
                    .pickSingleDay(callback)
                    .initiallyPickedSingleDay(primeCalendar)
                    .minPossibleDate(primeCalendar)
                    .firstDayOfWeek(Calendar.MONDAY)
                    .applyTheme(themeFactory)
                    .build()

                datePicker.show(supportFragmentManager, "AddingTaskDatePicker")
            }

            R.id.btn_begin_event_time -> {
                var tpd = TimePickerDialog(
                    this,
                    R.style.MyDatePickerStyle,
                    TimePickerDialog.OnTimeSetListener { view, sel_hour, sel_minutes ->

                        startTime = setUpTime(sel_hour, sel_minutes)
                        setTimeBlocks(startTime)

                    }, today_hour, today_minute,
                    true
                )
                tpd.show()
            }

            R.id.btn_end_event -> {
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

                val callback = SingleDayPickCallback { day ->
                    val localDate: LocalDate = day.getTime()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    setEndDateBlocks(localDate.toString())
                }

                val datePicker = PrimeDatePicker.dialogWith(primeCalendar)
                    .pickSingleDay(callback)
                    .initiallyPickedSingleDay(primeCalendar)
                    .minPossibleDate(primeCalendar)
                    .firstDayOfWeek(Calendar.MONDAY)
                    .applyTheme(themeFactory)
                    .build()

                datePicker.show(supportFragmentManager, "AddingTaskDatePicker")
            }

            R.id.btn_end_event_time -> {
                var tpde = TimePickerDialog(
                    this,
                    R.style.MyDatePickerStyle,
                    TimePickerDialog.OnTimeSetListener { view, sel_hour, sel_minutes ->

                        endTime = setUpTime(sel_hour, sel_minutes)
                        setEndTimeBlocks(endTime)

                    }, today_hour, today_minute,
                    true
                )
                tpde.show()
            }

            R.id.btn_cancel->
            {
                finish()
            }
        }
    }

    private fun resetRepeat() {
        when(repeat)
        {


            1->
            {
                binding.tvRepeatWeek.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }

            2->
            {
                binding.tvRepeatMonth.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }

            3->
            {
                binding.tvRepeatYear.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }
        }
    }

    private fun resetRemind() {
        when(reminder)
        {

            1->
            {
                binding.tvReminder15min.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }

            2->
            {
                binding.tvRemind1h.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }

            3->
            {
                binding.tvRemindDay.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }
        }
    }

    fun setUpDate(d: Int, m: Int, y: Int): String {
        var month = m.toString()
        var day = d.toString()

        month.takeIf { m  < 10 }?.let { month = "0" + (m) }
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
        binding.tvBeginEventHour.setText(table[0])
        binding.tvBeginEventMin.setText(table[1])
    }

    fun setEndTimeBlocks(date: String) {
        val table = date.split(':')
        binding.tvEndEventHour.setText(table[0])
        binding.tvEndEventMin.setText(table[1])
    }

    fun setDateBlocks(date: String) {
        val table = date.split('-')
        binding.tvBeginEventDay.setText(table[2])
        binding.tvBeginEventMonth.setText(table[1])
        binding.tvBeginEventYear.setText(table[0])
    }


    fun setEndDateBlocks(date: String) {
        val table = date.split('-')
        binding.tvEndEventDay.setText(table[2])
        binding.tvEndEventMonth.setText(table[1])
        binding.tvEndEventYear.setText(table[0])
    }

    fun setInitialDateBlocks(date: String) {
        val table = date.split('-')
        binding.tvBeginEventDay.setText(table[2])
        binding.tvBeginEventMonth.setText(table[1])
        binding.tvBeginEventYear.setText(table[0])

        binding.tvEndEventDay.setText(table[2])
        binding.tvEndEventMonth.setText(table[1])
        binding.tvEndEventYear.setText(table[0])
    }

    fun setInitialTimeBlocks(date: String) {
        val table = date.split(':')
        binding.tvBeginEventHour.setText(table[0])
        binding.tvBeginEventMin.setText(table[1])
        binding.tvEndEventHour.setText("23")
        binding.tvEndEventMin.setText("59")
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
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR_OF_DAY, 1)
        return dateFormat.format(calendar.time)
    }
}