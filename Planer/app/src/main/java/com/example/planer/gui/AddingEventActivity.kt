package com.example.planer.gui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.planer.MainActivity
import com.example.planer.R
import com.example.planer.ViewModel.CalendarViewModel
import com.example.planer.databinding.ActivityAddingTaskBinding
import com.example.planer.databinding.AddEventToCalendarBinding
import com.example.planer.gui.AddingEventActivity

import com.example.planer.entities.Tasks
import com.example.planer.gui.pages.CalendarFragment
import kotlinx.android.synthetic.main.activity_adding_task.*
import java.text.SimpleDateFormat
import java.util.*

class AddingEventActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: AddEventToCalendarBinding
    private lateinit var calendarViewModel: CalendarViewModel


    // zmienne do stworzenia nowego eventu

    var startDate : String = ""
    var startTime : String = ""
    var endTime : String = ""
    var endDate : String = ""
    var reminder: Int = 0 //0 - brak   1 - 15 min   2 - godzine przed   3 - dzień przed
    var repeat : Int = 0 // 0 - brak   1 - tydzień  2 - miesiąc         3 - rok
    var location: String = ""
    var noteId: Int = 0


    //calendar - pobranie aktualnego czasu
    val calendar = Calendar.getInstance()
    var today_year = calendar.get(Calendar.YEAR)
    var today_month = calendar.get(Calendar.MONTH)
    var today_day = calendar.get(Calendar.DAY_OF_MONTH)
    var today_hour = calendar.get(Calendar.HOUR_OF_DAY)
    var today_minute = calendar.get((Calendar.MINUTE))





    override fun onCreate(savedInstanceState: Bundle?) {

        val selectedDateMillis = intent.getLongExtra("selected_date_millis", 0L) // 0L is the default value if the key is not found
        val selectedDate = Date(selectedDateMillis) // create a Date object from the selected date in milliseconds
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val selectedDateString = dateFormat.format(selectedDate)


        Toast.makeText(this,selectedDateString,Toast.LENGTH_LONG).show()
        super.onCreate(savedInstanceState)

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

        today_day = selectedDateString.substring(8).toInt()
        today_month = selectedDateString.substring(5, 7).toInt()
        today_year= selectedDateString.substring(0, 4).toInt()




        setDateBlocks(setUpDate(today_day, today_month, today_year))
        setTimeBlocks(setUpTime(today_hour, today_minute))


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
                binding.tvRepeatWeek.getBackground().setTint((getResources().getColor(R.color.hard_red)))

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
                binding.tvRepeatMonth.getBackground().setTint((getResources().getColor(R.color.hard_red)))

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
                binding.tvRepeatYear.getBackground().setTint((getResources().getColor(R.color.hard_red)))

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
                binding.tvReminder15min.getBackground().setTint((getResources().getColor(R.color.hard_red)))

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
                binding.tvRemind1h.getBackground().setTint((getResources().getColor(R.color.hard_red)))

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
                binding.tvRemindDay.getBackground().setTint((getResources().getColor(R.color.hard_red)))

            }

            R.id.btn_create->{


                //val tasksDAO = (application as DatabaseApp).db.tasksDAO()
                //val intent = Intent(this, MainActivity::class.java)

                var dayb: String = binding.tvBeginEventDay.text.toString()
                if (dayb == "") {
                    dayb = today_day.toString()
                }       //????????????????? komunikat ze puste?

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


                var daye: String = binding.tvBeginEventDay.text.toString()
                if (daye == "") {
                    daye = today_day.toString()
                }       //????????????????? komunikat ze puste?

                var monthe: String = binding.tvBeginEventMonth.text.toString()
                if (monthe == "") {
                    monthe = today_month.toString()
                }

                var yeare: String = binding.tvBeginEventYear.text.toString()
                if (yeare == "") {
                    yeare = today_year.toString()
                }

                var houre: String = binding.tvBeginEventHour.text.toString()
                if (houre == "") {
                    houre = today_hour.toString()
                }

                var minutee: String = binding.tvBeginEventMin.text.toString()
                if (minutee == "") {
                    minutee = today_minute.toString()
                }

                // podłączenie się do bazy i dodanie do niej taska
                calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
                calendarViewModel.addCalendarDate(
                    com.example.planer.entities.Calendar(
                        startDate = setUpDate(  dayb.toInt(),
                            monthb.toInt(),
                            yearb.toInt()
                        ) + " " + setUpTime(hourb.toInt(), minuteb.toInt()),

                        endDate = setUpDate(  daye.toInt(),
                            monthe.toInt(),
                            yeare.toInt()
                        ) + " " + setUpTime(houre.toInt(), minutee.toInt()),
                        typeId = 0,
                        reminder = reminder,
                        location = location,
                        repeatId = repeat,
                        noteId = 0,
                        name = binding.eventTitle.text.toString()


                    )
                )
                Toast.makeText(applicationContext, "record saved", Toast.LENGTH_SHORT).show()

                //startActivity(intent)
                finish()

            }


            R.id.btn_begin_event -> {
                val dpd = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, sel_year, sel_month, sel_day ->

                        startDate = setUpDate(sel_day, sel_month, sel_year)
                        setDateBlocks(startDate)

                    }, today_year, today_month, today_day
                )

                dpd.show()
            }

            R.id.btn_begin_event_time -> {
                var tpd = TimePickerDialog(
                    this,
                    TimePickerDialog.OnTimeSetListener { view, sel_hour, sel_minutes ->

                        startTime = setUpTime(sel_hour, sel_minutes)
                        setTimeBlocks(startTime)

                    }, today_hour, today_minute,
                    true
                )
                tpd.show()
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
                binding.tvRepeatWeek.getBackground().setTint((getResources().getColor(R.color.green1)))
            }

            2->
            {
                binding.tvRepeatMonth.getBackground().setTint((getResources().getColor(R.color.green1)))
            }

            3->
            {
                binding.tvRepeatYear.getBackground().setTint((getResources().getColor(R.color.green1)))
            }
        }
    }


    private fun resetRemind() {
        when(reminder)
        {

            1->
            {
                binding.tvReminder15min.getBackground().setTint((getResources().getColor(R.color.green1)))
            }

            2->
            {
                binding.tvRemind1h.getBackground().setTint((getResources().getColor(R.color.green1)))
            }

            3->
            {
                binding.tvRemindDay.getBackground().setTint((getResources().getColor(R.color.green1)))
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
        binding.tvEndEventHour.setText("23")
        binding.tvEndEventMin.setText("59")
    }

    fun setDateBlocks(date: String) {
        val table = date.split('-')
        binding.tvBeginEventDay.setText(table[2])
        binding.tvBeginEventMonth.setText(table[1])
        binding.tvBeginEventYear.setText(table[0])

        binding.tvEndEventDay.setText(table[2])
        binding.tvEndEventMonth.setText(table[1])
        binding.tvEndEventYear.setText(table[0])
    }

}