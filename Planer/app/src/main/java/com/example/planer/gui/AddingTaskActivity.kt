package com.example.planer.gui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.planer.DatabaseApp
import com.example.planer.MainActivity
import com.example.planer.R
import com.example.planer.databinding.ActivityAddingTaskBinding

import com.example.planer.entities.Tasks
import com.example.planer.habits.Habit
import kotlinx.coroutines.launch
import java.util.*


// naprawic entery

class AddingTaskActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddingTaskBinding

    private var title: EditText? = null
//    private var priority0: ImageView? = null
//    private var priority1: ImageView? = null
//    private var priority2: ImageView? = null
//    private var priority3: ImageView? = null
//
//    private var urgent0: ImageView? = null
//    private var urgent1: ImageView? = null
//    private var urgent2: ImageView? = null
//    private var urgent3: ImageView? = null

    private var deadline_day: EditText? = null
    private var deadline_month: EditText? = null
    private var deadline_year: EditText? = null

    private var deadline_hour: EditText? = null
    private var deadline_minutes: EditText? = null

    private var deadline_picker: ImageButton? = null
    private var deadline_time_picker: ImageButton? = null


    private var note: EditText? = null
    private var create: Button? = null
    private var cancel: Button? = null

    //calendar
    val calendar = Calendar.getInstance()
    val today_year = calendar.get(Calendar.YEAR)
    val today_month = calendar.get(Calendar.MONTH)
    val today_day = calendar.get(Calendar.DAY_OF_MONTH)
    val today_hour = calendar.get(Calendar.HOUR_OF_DAY)
    val today_minute = calendar.get((Calendar.MINUTE))
    var date: String = ""
    var time: String = ""

    //global
    var important: Int = 0
    var urgent: Int = 0
    var type: Int = 0
    var duration: Int = 0
    var periodicity: Int = 0
    var note_txt: Int = 0

    var tasksList: ArrayList<Habit>? = null


    lateinit var duration1: ImageView
    lateinit var duration2: ImageView
    lateinit var duration3: ImageView
    lateinit var duration4: ImageView

    lateinit var type1: ImageView
    lateinit var type2: ImageView
    lateinit var type3: ImageView
    lateinit var type4: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_adding_task)

        binding = ActivityAddingTaskBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        title = findViewById(R.id.task_title)
//        priority0 = findViewById(R.id.priority0)
//        priority1 = findViewById(R.id.priority1)
//        priority2 = findViewById(R.id.priority2)
//        priority3 = findViewById(R.id.priority3)

//        urgent0 = findViewById(R.id.urgent0)
//        urgent1 = findViewById(R.id.urgent1)
//        urgent2 = findViewById(R.id.urgent2)
//        urgent3 = findViewById(R.id.urgent3)

        deadline_day = findViewById(R.id.tv_deadline_d)
        deadline_month = findViewById(R.id.tv_deadline_m)
        deadline_year = findViewById(R.id.tv_deadline_y)

        deadline_hour = findViewById(R.id.tv_deadline_h)
        deadline_minutes = findViewById(R.id.tv_deadline_min)

        deadline_picker = findViewById(R.id.btn_deadline)
        deadline_time_picker = findViewById(R.id.btn_deadline_time)


        note = findViewById(R.id.note)
        create = findViewById(R.id.btn_create)
        cancel = findViewById(R.id.btn_cancel)


        create?.setOnClickListener(this)
        cancel?.setOnClickListener(this)
        deadline_picker?.setOnClickListener(this)
        deadline_time_picker?.setOnClickListener(this)

//        priority0?.setOnClickListener(this)
//        priority1?.setOnClickListener(this)
//        priority2?.setOnClickListener(this)
//        priority3?.setOnClickListener(this)

        binding.important0.setOnClickListener(this)
        binding.important1.setOnClickListener(this)
        binding.important2.setOnClickListener(this)
        binding.important3.setOnClickListener(this)

        binding.urgent0.setOnClickListener(this)
        binding.urgent1.setOnClickListener(this)
        binding.urgent2.setOnClickListener(this)
        binding.urgent3.setOnClickListener(this)

        deadline_day?.setOnClickListener(this)
        deadline_month?.setOnClickListener(this)
        deadline_year?.setOnClickListener(this)

        duration1 = binding.duration1
        duration2 = binding.duration2
        duration3 = binding.duration3
        duration4 = binding.duration4

        duration1.setOnClickListener(this)
        duration2.setOnClickListener(this)
        duration3.setOnClickListener(this)
        duration4.setOnClickListener(this)


        type1 = binding.type1
        type2 = binding.type2
        type3 = binding.type3
        type4 = binding.type4

        type1.setOnClickListener(this)
        type2.setOnClickListener(this)
        type3.setOnClickListener(this)
        type4.setOnClickListener(this)

        setDateBlocks(setUpDate(today_day, today_month, today_year))
        setTimeBlocks(setUpTime(today_hour, today_minute))

        setEverything()

    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.duration1 -> {
                uncheckDuration()
                duration1.setColorFilter(getResources().getColor(R.color.hard_red));
                duration = 0
            }

            R.id.duration2 -> {
                uncheckDuration()
                duration2.setColorFilter(getResources().getColor(R.color.hard_red));
                duration = 1
            }

            R.id.duration3 -> {
                uncheckDuration()
                duration3.setColorFilter(getResources().getColor(R.color.hard_red));
                duration = 2
            }

            R.id.duration4 -> {
                uncheckDuration()
                duration4.setColorFilter(getResources().getColor(R.color.hard_red));
                duration = 3
            }


            R.id.type1 -> {
                uncheckType()
                type1.setColorFilter(getResources().getColor(R.color.hard_red));
                type = 0
            }

            R.id.type2 -> {
                uncheckType()
                type2.setColorFilter(getResources().getColor(R.color.hard_red));
                type = 1
            }

            R.id.type3 -> {
                uncheckType()
                type3.setColorFilter(getResources().getColor(R.color.hard_red));
                type = 2
            }

            R.id.type4 -> {
                uncheckType()
                type4.setColorFilter(getResources().getColor(R.color.hard_red));
                type = 3
            }


            R.id.btn_create -> {

                val tasksDAO = (application as DatabaseApp).db.tasksDAO()
                //val intent = Intent(this, MainActivity::class.java)

                var day: String = deadline_day?.text.toString()
                if (day == "") {
                    day = today_day.toString()
                }       //????????????????? komunikat ze puste?

                var month: String = deadline_month?.text.toString()
                if (month == "") {
                    month = today_month.toString()
                }

                var year: String = deadline_year?.text.toString()
                if (year == "") {
                    year = today_year.toString()
                }

                var hour: String = deadline_hour?.text.toString()
                if (hour == "") {
                    hour = today_hour.toString()
                }

                var minute: String = deadline_day?.text.toString()
                if (minute == "") {
                    minute = today_minute.toString()
                }

                //?????????????? jak to zrobic


                lifecycleScope.launch {
                    tasksDAO.insert(
                        Tasks(
                            title = title?.text.toString(),
                            importance = 0,           //???????????????

                            /*TODO uwzględnienie pilności w bazie (nieobowiązkowe)*/
                            //urgent = urgent,

                            deadline = setUpDate(
                                day.toInt(),
                                month.toInt(),
                                year.toInt()
                            ) + "  " + setUpTime(hour.toInt(), minute.toInt()),
                            timeToFinish = 1,
                            isActive = 0,
                            typeId = 0,
                            noteId = 0,
                            urgency = 0
                        )
                    )
                    Toast.makeText(applicationContext, "record saved", Toast.LENGTH_SHORT).show()
                }


                //startActivity(intent)
                finish()

            }

            R.id.btn_cancel -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }


            R.id.important0 -> {
                uncheckImportant()

//                val priority_col_0 = (priority0?.getBackground() as GradientDrawable).mutate()
//                (priority_col_0 as GradientDrawable).setColor(Color.argb(90,0,0,0))

                binding.important0?.setImageResource(R.drawable.radio_checked)
                important = 0

            }
            R.id.important1 -> {
                uncheckImportant()
                binding.important1?.setImageResource(R.drawable.radio_checked)
                important = 1
            }

            R.id.important2 -> {
                uncheckImportant()
                binding.important2?.setImageResource(R.drawable.radio_checked)
                important = 2
            }
            R.id.important3 -> {
                uncheckImportant()
                binding.important3?.setImageResource(R.drawable.radio_checked)
                important = 3
            }

            R.id.urgent0 -> {
                uncheckUrgent()
                binding.urgent0?.setImageResource(R.drawable.radio_checked)
                urgent = 0
            }

            R.id.urgent1 -> {
                uncheckUrgent()
                binding.urgent1?.setImageResource(R.drawable.radio_checked)
                urgent = 1
            }

            R.id.urgent2 -> {
                uncheckUrgent()
                binding.urgent2?.setImageResource(R.drawable.radio_checked)
                urgent = 2
            }

            R.id.urgent3 -> {
                uncheckUrgent()
                binding.urgent3?.setImageResource(R.drawable.radio_checked)
                urgent = 3
            }


            R.id.btn_deadline -> {
                val dpd = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, sel_year, sel_month, sel_day ->

                        date = setUpDate(sel_day, sel_month, sel_year)
                        setDateBlocks(date)

                    }, today_year, today_month, today_day
                )

                dpd.show()
            }

            R.id.btn_deadline_time -> {
                var tpd = TimePickerDialog(
                    this,
                    TimePickerDialog.OnTimeSetListener { view, sel_hour, sel_minutes ->

                        time = setUpTime(sel_hour, sel_minutes)
                        setTimeBlocks(time)

                    }, today_hour, today_minute,
                    true
                )
                tpd.show()
            }
        }
    }


    fun setUpDate(d: Int, m: Int, y: Int): String {
        var month = m.toString()
        var day = d.toString()

        month.takeIf { m + 1 < 10 }?.let { month = "0" + (m + 1) }
        day.takeIf { d < 10 }?.let { day = "0" + d }

        return "$day.$month.$y"
    }

    fun setDateBlocks(date: String) {
        val table = date.split('.')
        deadline_day?.setText(table[0])
        deadline_month?.setText(table[1])
        deadline_year?.setText(table[2])
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
        deadline_hour?.setText(table[0])
        deadline_minutes?.setText(table[1])
    }


    fun setEverything() {

        deadline_day?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                deadline_minutes?.clearFocus()
                deadline_hour?.clearFocus()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! > 1) {

                    deadline_month?.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        deadline_month?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                deadline_minutes?.clearFocus()
                deadline_hour?.clearFocus()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                if (s?.length!! > 1) {
                    deadline_year?.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        deadline_year?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                deadline_minutes?.clearFocus()
                deadline_hour?.clearFocus()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                if (s?.length!! >= 4) {
                    deadline_year?.clearFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        deadline_hour?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                deadline_year?.clearFocus()
                deadline_month?.clearFocus()
                deadline_day?.clearFocus()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s?.length!! > 1) {
                    deadline_minutes?.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        deadline_minutes?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                deadline_year?.clearFocus()
                deadline_month?.clearFocus()
                deadline_day?.clearFocus()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                if (s?.length!! > 1) {
                    deadline_minutes?.clearFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        //ustawianie filtrów
//        deadline_minutes?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))
//        deadline_hour?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))
//        deadline_day?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))
//        deadline_month?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))
//        deadline_year?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(4))


        setOnEnterKey()
        setOnFocusChange()

    }

    fun uncheckType() {
        when (type) {
            0 -> {
                binding.type1.setColorFilter(getResources().getColor(R.color.green1));
            }

            1 -> {
                binding.type2.setColorFilter(getResources().getColor(R.color.green1));
            }

            2 -> {
                binding.type3.setColorFilter(getResources().getColor(R.color.green1));
            }

            3 -> {
                binding.type4.setColorFilter(getResources().getColor(R.color.green1));
            }
        }
    }

    fun uncheckDuration() {
        when (duration) {
            0 -> {
                binding.duration1.setColorFilter(getResources().getColor(R.color.green1));
            }

            1 -> {
                binding.duration2.setColorFilter(getResources().getColor(R.color.green1));
            }

            2 -> {
                binding.duration3.setColorFilter(getResources().getColor(R.color.green1));
            }

            3 -> {
                binding.duration4.setColorFilter(getResources().getColor(R.color.green1));
            }
        }
    }

    fun uncheckImportant() {
        when (important) {
            0 -> {
                //val gradientDrawable = (priority0?.getBackground() as GradientDrawable).mutate()
                //(gradientDrawable as GradientDrawable).setColor(Color.argb(0,0,0,0))

                binding.important0?.setImageResource(R.drawable.radio_unchecked)
            }
            1 -> {
                binding.important1?.setImageResource(R.drawable.radio_unchecked)
            }
            2 -> {
                binding.important2?.setImageResource(R.drawable.radio_unchecked)
            }
            3 -> {
                binding.important3?.setImageResource(R.drawable.radio_unchecked)
            }
        }
    }

    fun uncheckUrgent() {
        when (urgent) {
            0 -> {
                //val gradientDrawable = (priority0?.getBackground() as GradientDrawable).mutate()
                //(gradientDrawable as GradientDrawable).setColor(Color.argb(0,0,0,0))

                binding.urgent0?.setImageResource(R.drawable.radio_unchecked)
            }
            1 -> {
                binding.urgent1?.setImageResource(R.drawable.radio_unchecked)
            }
            2 -> {
                binding.urgent2?.setImageResource(R.drawable.radio_unchecked)
            }
            3 -> {
                binding.urgent3?.setImageResource(R.drawable.radio_unchecked)
            }
        }
    }

    fun setOnEnterKey() {
        // ustawienie limitu dlugosci wprowadzanych liczb
        deadline_day?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                deadline_day?.clearFocus()
                deadline_month?.requestFocus()

//                var d = deadline_day?.text.toString()
//                if(d.trim().length > 0 && d.trim().length < 10)
//                {
//                    deadline_day?.setText("0$d")
//                }
//                else if(d.trim().length > 9 && d.trim().length < 32)
//                {
//                    deadline_day?.setText(d)
//                }
//                else
//                {
//                    deadline_day?.setText("01")
//                }

                return@OnKeyListener true
            }
            false
        })

        deadline_month?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                deadline_month?.clearFocus()
                deadline_year?.requestFocus()

                return@OnKeyListener true
            }
            false
        })

        deadline_year?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                deadline_year?.clearFocus()
                return@OnKeyListener true
            }
            false
        })

        deadline_hour?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                deadline_hour?.clearFocus()
                deadline_minutes?.requestFocus()
                return@OnKeyListener true
            }
            false
        })

        deadline_minutes?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                deadline_minutes?.clearFocus()

                return@OnKeyListener true
            }
            false
        })

//
//        Listener(View.OnKeyListener { v, keyCode, event ->
//            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
//                title?.clearFocus()
//                return@OnKeyListener true
//            }
//            false
//        })
    }

    fun setOnFocusChange() {
        // czyszczenie pola po nacisnieciu na nie
        deadline_day?.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                deadline_day?.text?.clear()
            } else {
                var tmp = deadline_day?.text.toString()
                if (tmp != "") {
                    if (tmp.toInt() >= 0 && tmp.toInt() < 10) {
                        deadline_day?.setText("0" + tmp.toInt())
                    } else if (tmp.toInt() > 31) {
                        deadline_day?.setText(
                            setUpDate(
                                today_day,
                                today_month,
                                today_year
                            ).substring(0, 2)
                        )

                    }
                }
//                else
//                {
//                    deadline_day?.setText(setUpDate(today_day, today_month, today_year).substring(0, 2))
//                }
            }
        })

        deadline_month?.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                deadline_month?.text?.clear()
            } else {
                var tmp = deadline_month?.text.toString()
                if (tmp != "") {
                    if (tmp.toInt() >= 0 && tmp.toInt() < 10) {
                        deadline_month?.setText("0" + tmp.toInt())
                    } else if (tmp.toInt() > 12) {
                        deadline_month?.setText(
                            setUpDate(
                                today_day,
                                today_month,
                                today_year
                            ).substring(3, 5)
                        )

                    }

                }
//                else
//                {
//                    deadline_month?.setText(setUpDate(today_day, today_month, today_year).substring(3, 5))
//                }
            }
        })

        deadline_year?.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                deadline_year?.text?.clear()
            } else {

                var tmp = deadline_year?.text.toString()
                if (tmp != "") {
                    if (tmp.toInt() < today_year - 2)       //????????????
                    {
                        deadline_year?.setText(
                            setUpDate(
                                today_day,
                                today_month,
                                today_year
                            ).substring(6, 10)
                        )

                    }

                }
//                else
//                {
//                    deadline_year?.setText(setUpDate(today_day, today_month, today_year).substring(6, 10))
//                }
            }
        })

        deadline_hour?.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                deadline_hour?.text?.clear()
            } else {

                var tmp = deadline_hour?.text.toString()
                if (tmp != "") {
                    if (tmp.toInt() >= 0 && tmp.toInt() < 10) {
                        deadline_hour?.setText("0" + tmp.toInt())
                    } else if (tmp.toInt() > 23)       //????????????
                    {
                        deadline_hour?.setText(
                            setUpTime(today_hour, today_minute).substring(
                                0,
                                2
                            )
                        )  //??????????

                    }

                }
//                else
//                {
//                    deadline_hour?.setText(setUpTime(today_hour, today_minute).substring(0,2))
//                }
            }

        })

        deadline_minutes?.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                deadline_minutes?.text?.clear()
            } else {
                var tmp = deadline_minutes?.text.toString()
                if (tmp != "") {
                    if (tmp.toInt() >= 0 && tmp.toInt() < 10) {
                        deadline_minutes?.setText("0" + tmp.toInt())
                    } else if (tmp.toInt() > 59)       //????????????
                    {
                        deadline_minutes?.setText(
                            setUpTime(today_hour, today_minute).substring(
                                3,
                                5
                            )
                        )  //????????
                    }

                }
//                else
//                {
//                    deadline_minutes?.setText(setUpTime(today_hour, today_minute).substring(3,5))
//                }
            }
        })


    }
}

// Toast.makeText(this, "nic", Toast.LENGTH_SHORT).show()
