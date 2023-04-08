package com.example.planer.gui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import com.example.planer.MainActivity
import com.example.planer.R
import com.example.planer.ViewModel.TaskViewModel
import com.example.planer.databinding.ActivityAddingTaskBinding

import com.example.planer.entities.Tasks
import java.util.*


// naprawic entery

class AddingTaskActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddingTaskBinding
    private lateinit var taskViewModel: TaskViewModel


    // zmienne do stworzenia nowego tasku
    var important: Int = 0
    var urgent: Int = 0
    var type: Int = 0
    var duration: Int = 0
    var periodicity: Int = 0
    var note_txt: Int = 0
    var date: String = ""
    var time: String = ""
    var specyficDate: Int = 0   //0 - deadline, 1 - konkretyny dzien


    //calendar - pobranie aktualnego czasu
    val calendar = Calendar.getInstance()
    val today_year = calendar.get(Calendar.YEAR)
    val today_month = calendar.get(Calendar.MONTH)
    val today_day = calendar.get(Calendar.DAY_OF_MONTH)
    val today_hour = calendar.get(Calendar.HOUR_OF_DAY)
    val today_minute = calendar.get((Calendar.MINUTE))

    var chosenItems = mutableListOf<String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_adding_task)

        binding = ActivityAddingTaskBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        // aktywowanie możliwości 'klikania' na obiekty
        binding.btnCreate.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        binding.btnDeadline.setOnClickListener(this)
        binding.btnDeadlineTime.setOnClickListener(this)

        binding.btnPeriodicity.setOnClickListener(this)


        binding.important0.setOnClickListener(this)
        binding.important1.setOnClickListener(this)


        binding.urgent0.setOnClickListener(this)
        binding.urgent1.setOnClickListener(this)

        binding.tvDeadlineD.setOnClickListener(this)
        binding.tvDeadlineM.setOnClickListener(this)
        binding.tvDeadlineY.setOnClickListener(this)

        binding.atThisDay.setOnClickListener(this)
        binding.toThisDay.setOnClickListener(this)

        binding.duration1.setOnClickListener(this)
        binding.duration2.setOnClickListener(this)
        binding.duration3.setOnClickListener(this)
        binding.duration4.setOnClickListener(this)
        binding.duration5.setOnClickListener(this)
        binding.duration6.setOnClickListener(this)

        binding.type1.setOnClickListener(this)
        binding.type2.setOnClickListener(this)
        binding.type3.setOnClickListener(this)
        binding.type4.setOnClickListener(this)

        setDateBlocks(setUpDate(today_day, today_month, today_year))
        setTimeBlocks(setUpTime(today_hour, today_minute))

        chosenItems.add("kliknij aby usunąć")

        setEverything()

    }

    override fun onClick(v: View?) {

        when (v?.id) {

            // zmiana koloru przy wyborze
            R.id.duration1 -> {
                uncheckDuration()
                //binding.duration1.setColorFilter(getResources().getColor(R.color.hard_red));
                binding.duration1.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_on)))

                duration = 0
            }

            R.id.duration2 -> {
                uncheckDuration()
                //binding.duration2.setColorFilter(getResources().getColor(R.color.hard_red));
                binding.duration2.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_on)))

                duration = 1
            }

            R.id.duration3 -> {
                uncheckDuration()
                //binding.duration3.setColorFilter(getResources().getColor(R.color.hard_red));
                binding.duration3.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_on)))

                duration = 2
            }

            R.id.duration4 -> {
                uncheckDuration()
                //binding.duration4.setColorFilter(getResources().getColor(R.color.hard_red));
                binding.duration4.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_on)))

                duration = 3
            }

            R.id.duration5 -> {
                uncheckDuration()
                binding.duration5.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_on)))
                duration = 4
            }

            R.id.duration6 -> {
                uncheckDuration()
                binding.duration6.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_on)))
                duration = 5
            }



            R.id.type1 -> {
                uncheckType()
                binding.type1.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_on)))
                type = 0
            }

            R.id.type2 -> {
                uncheckType()
                binding.type2.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_on)))
                type = 1
            }

            R.id.type3 -> {
                uncheckType()
                binding.type3.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_on)))
                type = 2
            }

            R.id.type4 -> {
                uncheckType()
                binding.type4.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_on)))
                type = 3
            }


            R.id.at_this_day -> {
                binding.toThisDay.setTextColor(getResources().getColor(R.color.deadline_not_chosen))
                binding.atThisDay.setTextColor(getResources().getColor(R.color.brown_important_urgent_on))
                specyficDate = 1
            }

            R.id.to_this_day -> {
                binding.atThisDay.setTextColor(getResources().getColor(R.color.deadline_not_chosen))
                binding.toThisDay.setTextColor(getResources().getColor(R.color.brown_important_urgent_on))

                specyficDate = 0
            }


            // pobieranie danych do stworzenia taska
            R.id.btn_create -> {

                //val tasksDAO = (application as DatabaseApp).db.tasksDAO()
                //val intent = Intent(this, MainActivity::class.java)

                var day: String = binding.tvDeadlineD.text.toString()
                if (day == "") {
                    day = today_day.toString()
                }       //????????????????? komunikat ze puste?

                var month: String = binding.tvDeadlineM.text.toString()
                if (month == "") {
                    month = today_month.toString()
                }

                var year: String = binding.tvDeadlineY.text.toString()
                if (year == "") {
                    year = today_year.toString()
                }

                var hour: String = binding.tvDeadlineH.text.toString()
                if (hour == "") {
                    hour = today_hour.toString()
                }

                var minute: String = binding.tvDeadlineMin.text.toString()
                if (minute == "") {
                    minute = today_minute.toString()
                }

                // podłączenie się do bazy i dodanie do niej taska
                taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
                taskViewModel.addTask(
                    Tasks(
                        title = binding.taskTitle.text.toString(),
                        importance = important,           //???????????????

                        /*TODO uwzględnienie pilności w bazie (nieobowiązkowe)*/
                        urgency = urgent,
                        deadline = setUpDate(day.toInt(),month.toInt()-1, year.toInt()) + "  " +
                                setUpTime(hour.toInt(), minute.toInt()),
                        timeToFinish = 1,
                        isActive = 0,
                        typeId = type,
                        noteId = 0,
                        //date = Calendar.getInstance().time //Ustawianie czasu na domyslny
                        //date = null
                    )
                )
                Toast.makeText(applicationContext, "record saved", Toast.LENGTH_SHORT).show()

                //startActivity(intent)
                finish()

            }

            R.id.btn_cancel -> {
                val intent = Intent(this, MainActivity::class.java)
                //startActivity(intent)
                finish()
            }


            R.id.important0 -> {
                uncheckImportant()

//                val priority_col_0 = (priority0?.getBackground() as GradientDrawable).mutate()
//                (priority_col_0 as GradientDrawable).setColor(Color.argb(90,0,0,0))

                binding.important0.setBackgroundColor(getResources().getColor(R.color.brown_important_urgent_on));
                important = 0

            }
            R.id.important1 -> {
                uncheckImportant()
                binding.important1.setBackgroundColor(getResources().getColor(R.color.brown_important_urgent_on));
                important = 1
            }


            R.id.urgent0 -> {
                uncheckUrgent()
                binding.urgent0.setBackgroundColor(getResources().getColor(R.color.brown_important_urgent_on));
                urgent = 0
            }

            R.id.urgent1 -> {
                uncheckUrgent()
                binding.urgent1.setBackgroundColor(getResources().getColor(R.color.brown_important_urgent_on));
                urgent = 1
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

            R.id.btn_periodicity -> {

                val dpd = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, sel_year, sel_month, sel_day ->

                        //date = setUpDate(sel_day, sel_month, sel_year)
                        //setDateBlocks(date)

                        chosenItems.add(setUpDate(sel_day, sel_month, sel_year))

                    }, today_year, today_month, today_day
                )

                dpd.show()

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, chosenItems)
                binding.spinner.adapter = adapter
                binding.spinner.setSelection(0)
                binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        // remove the selected item from the spinner's adapter
                        if(!adapter.getItem(position).equals("kliknij aby usunąć"))
                        {
                            adapter.remove(adapter.getItem(position))
                            binding.spinner.setSelection(0)
                            // notify the adapter that the data set has changed
                            adapter.notifyDataSetChanged()
                        }

                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        binding.spinner.setSelection(0)
                    }
                }
            }
        }
    }

    // formatowanie daty
    fun setUpDate(d: Int, m: Int, y: Int): String {
        var month = m.toString()
        var day = d.toString()

        month.takeIf { m + 1 < 10 }?.let { month = "0" + (m + 1) }
        day.takeIf { d < 10 }?.let { day = "0" + d }

        return "$y-$month-$day"
    }

    // wpisanie aktualnej daty do pól w tworzeniu tasków - takie ustalenie domyślej daty przy tworzeniu
    fun setDateBlocks(date: String) {
        val table = date.split('-')
        binding.tvDeadlineY.setText(table[0])
        binding.tvDeadlineM.setText(table[1])
        binding.tvDeadlineD.setText(table[2])
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
        binding.tvDeadlineH.setText(table[0])
        binding.tvDeadlineMin.setText(table[1])
    }


    // funkcja gromadząca większość ustawień (np. enterów)
    fun setEverything() {

        /*
        binding.tvDeadlineD.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.tvDeadlineMin.clearFocus()
                binding.tvDeadlineH.clearFocus()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! > 1) {

                    binding.tvDeadlineM.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.tvDeadlineM.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.tvDeadlineMin.clearFocus()
                binding.tvDeadlineH.clearFocus()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                if (s?.length!! > 1) {
                    binding.tvDeadlineY.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.tvDeadlineY.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.tvDeadlineMin.clearFocus()
                binding.tvDeadlineH.clearFocus()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s?.length!! >= 4) {
                    binding.tvDeadlineY.clearFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.tvDeadlineH.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.tvDeadlineY.clearFocus()
                binding.tvDeadlineM.clearFocus()
                binding.tvDeadlineD.clearFocus()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s?.length!! > 1) {
                    binding.tvDeadlineMin.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.tvDeadlineMin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.tvDeadlineY.clearFocus()
                binding.tvDeadlineM.clearFocus()
                binding.tvDeadlineD.clearFocus()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                if (s?.length!! > 1) {
                    binding.tvDeadlineMin.clearFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

         */

        //ustawianie filtrów
//        deadline_minutes?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))
//        deadline_hour?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))
//        deadline_day?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))
//        deadline_month?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))
//        deadline_year?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(4))


        //setOnEnterKey()
        setOnFocusChange()

    }

    fun uncheckType() {
        when (type) {
            0 -> {
                binding.type1.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }

            1 -> {
                binding.type2.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }

            2 -> {
                binding.type3.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }

            3 -> {
                binding.type4.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }
        }
    }

    fun uncheckDuration() {
        when (duration) {
            0 -> {
                binding.duration1.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }

            1 -> {
                binding.duration2.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }

            2 -> {
                binding.duration3.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }

            3 -> {
                binding.duration4.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }

            4 -> {
                binding.duration5.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_off)))

            }

            5 -> {
                binding.duration6.getBackground().setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }
        }
    }

    fun uncheckImportant() {
        when (important) {
            0 -> {
                //val gradientDrawable = (priority0?.getBackground() as GradientDrawable).mutate()
                //(gradientDrawable as GradientDrawable).setColor(Color.argb(0,0,0,0))

                binding.important0.setBackgroundColor(getResources().getColor(R.color.brown_important_urgent_off))
            }
            1 -> {
                binding.important1.setBackgroundColor(getResources().getColor(R.color.brown_important_urgent_off))
            }
        }
    }

    fun uncheckUrgent() {
        when (urgent) {
            0 -> {
                //val gradientDrawable = (priority0?.getBackground() as GradientDrawable).mutate()
                //(gradientDrawable as GradientDrawable).setColor(Color.argb(0,0,0,0))

                binding.urgent0.setBackgroundColor(getResources().getColor(R.color.brown_important_urgent_off));
            }
            1 -> {
                binding.urgent1.setBackgroundColor(getResources().getColor(R.color.brown_important_urgent_off))
            }

        }
    }

    /*
    fun setOnEnterKey() {

        // ustawienie limitu dlugosci wprowadzanych liczb
        binding.tvDeadlineD.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                binding.tvDeadlineD.clearFocus()
                binding.tvDeadlineM.requestFocus()

                return@OnKeyListener true
            }
            false
        })

        binding.tvDeadlineM.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                binding.tvDeadlineM.clearFocus()
                binding.tvDeadlineY.requestFocus()

                return@OnKeyListener true
            }
            false
        })

        binding.tvDeadlineY.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                binding.tvDeadlineY.clearFocus()
                return@OnKeyListener true
            }
            false
        })

        binding.tvDeadlineH.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                binding.tvDeadlineH.clearFocus()
                binding.tvDeadlineMin.requestFocus()
                return@OnKeyListener true
            }
            false
        })

        binding.tvDeadlineMin.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                binding.tvDeadlineMin.clearFocus()

                return@OnKeyListener true
            }
            false
        })

    }

   */

    fun setOnFocusChange() {
        // czyszczenie pola po nacisnieciu na nie
        binding.tvDeadlineD.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.tvDeadlineD.text?.clear()
            } else {
                var tmp = binding.tvDeadlineD.text.toString()
                if (tmp != "") {
                    if (tmp.toInt() >= 0 && tmp.toInt() < 10) {
                        binding.tvDeadlineD.setText("0" + tmp.toInt())
                    } else if (tmp.toInt() > 31) {
                        binding.tvDeadlineD.setText(
                            setUpDate(
                                today_day,
                                today_month,
                                today_year
                            ).substring(8, 10)
                        )

                        // 0123456789012345
                        // yyyy-mm-dd hh:mm

                    }
                }

                //binding.tvDeadlineD.clearFocus()
//                else
//                {
//                    deadline_day?.setText(setUpDate(today_day, today_month, today_year).substring(0, 2))
//                }
            }

            binding.tvDeadlineD.clearFocus()

        })

        binding.tvDeadlineM.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.tvDeadlineM.text?.clear()
            } else {
                var tmp = binding.tvDeadlineM.text.toString()
                if (tmp != "") {
                    if (tmp.toInt() >= 0 && tmp.toInt() < 10) {
                        binding.tvDeadlineM.setText("0" + tmp.toInt())
                    } else if (tmp.toInt() > 12) {
                        binding.tvDeadlineM.setText(
                            setUpDate(
                                today_day,
                                today_month,
                                today_year
                            ).substring(5, 7)
                        )
                        // 0123456789012345
                        // yyyy-mm-dd hh:mm

                    }

                }

            }
            binding.tvDeadlineM.clearFocus()

        })

        binding.tvDeadlineY.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.tvDeadlineY.text?.clear()
            } else {

                var tmp = binding.tvDeadlineY.text.toString()
                if (tmp != "") {
                    if (tmp.toInt() < today_year - 2)       //????????????
                    {
                        binding.tvDeadlineY.setText(
                            setUpDate(
                                today_day,
                                today_month,
                                today_year
                            ).substring(0, 4)
                        )
                        // 0123456789012345
                        // yyyy-mm-dd hh:mm
                    }

                }
            }
            binding.tvDeadlineY.clearFocus()

        })

        binding.tvDeadlineH.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.tvDeadlineH.text?.clear()
            } else {

                var tmp = binding.tvDeadlineH.text.toString()
                if (tmp != "") {
                    if (tmp.toInt() >= 0 && tmp.toInt() < 10) {
                        binding.tvDeadlineH.setText("0" + tmp.toInt())
                    } else if (tmp.toInt() > 23)       //????????????
                    {
                        binding.tvDeadlineH.setText(
                            setUpTime(today_hour, today_minute)
                                .substring(0, 2)
                        )  //??????????

                    }

                }

            }
            binding.tvDeadlineH.clearFocus()


        })

        binding.tvDeadlineMin.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.tvDeadlineMin.text?.clear()
            } else {
                var tmp = binding.tvDeadlineMin.text.toString()
                if (tmp != "") {
                    if (tmp.toInt() >= 0 && tmp.toInt() < 10) {
                        binding.tvDeadlineMin.setText("0" + tmp.toInt())
                    } else if (tmp.toInt() > 59)       //????????????
                    {
                        binding.tvDeadlineMin.setText(
                            setUpTime(today_hour, today_minute).substring(3, 5)
                        )  //????????
                    }

                }

            }
            binding.tvDeadlineMin.clearFocus()

        })


    }

}
