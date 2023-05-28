package com.top.planer.gui.pages.home.tasks

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.SparseIntArray
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.common.BackgroundShapeType
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.aminography.primedatepicker.picker.theme.LightThemeFactory
import com.top.planer.R
import com.top.planer.ViewModel.TaskViewModel
import com.top.planer.ViewModel.TypeViewModel
import com.top.planer.databinding.ActivityAddingTaskBinding
import com.top.planer.entities.Notes

import com.top.planer.entities.Tasks
import kotlinx.android.synthetic.main.dialod_when_title_empty.view.*
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
// naprawic entery
class AddingTaskActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddingTaskBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var typeViewModel: TypeViewModel

    // Lista dat w których task ma być powtarzany
    private var markedDatePickerList: MutableList<PrimeCalendar> = mutableListOf()

    // zmienne do stworzenia nowego tasku
    var important: Int = 0
    var urgent: Int = 0
    var type: Int? = null
    var duration: Int = 1
    var periodicity: Int = 0
    var note_txt: Int = 0
    var date: String = ""
    var time: String = ""
    var specyficDate: Int = 0   //0 - deadline, 1 - konkretyny dzien

    //calendar - pobranie aktualnego czasu
    private val primeCalendar = CivilCalendar(TimeZone.getDefault(), Locale("pl", "PL"))

    val calendar = Calendar.getInstance()
    val today_year = calendar.get(Calendar.YEAR)
    val today_month = calendar.get(Calendar.MONTH)
    val today_day = calendar.get(Calendar.DAY_OF_MONTH)
    val today_hour = 12//calendar.get(Calendar.HOUR_OF_DAY)
    val today_minute = 0//calendar.get((Calendar.MINUTE))

    //var chosenItems = mutableListOf<String>()
    var listOfTypes = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Set full screen
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)

        binding = ActivityAddingTaskBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding.taskTitle.requestFocus()

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

        setDateBlocks(setUpDate(today_day, today_month, today_year))
        setTimeBlocks(setUpTime(today_hour, today_minute))


        typeViewModel = ViewModelProvider(this)[TypeViewModel::class.java]
        typeViewModel.readAllData.observe(this) {

            listOfTypes = mutableListOf()
            listOfTypes.add("Brak")
            for(type in it)
            {
                listOfTypes.add(type.name)
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOfTypes)
            binding.typeSpinner.adapter = adapter
            binding.typeSpinner.setSelection(0)
            binding.typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long)
                {
                    type = if (position == 0) null else position
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    binding.typeSpinner.setSelection(0)
                }
            }
        }
        setEverything()
    }
    override fun onClick(v: View?) {

        when (v?.id) {
            // zmiana koloru przy wyborze
            R.id.duration1 -> {
                uncheckDuration()
                binding.duration1.getBackground()
                    .setTint((getResources().getColor(R.color.brown_important_urgent_on)))

                duration = 1
            }

            R.id.duration2 -> {
                uncheckDuration()

                if (duration == 2) {
                    binding.duration1.getBackground()
                        .setTint((getResources().getColor(R.color.brown_important_urgent_on)))
                    duration = 1
                } else {
                    binding.duration2.getBackground()
                        .setTint((getResources().getColor(R.color.brown_important_urgent_on)))
                    duration = 2
                }


            }

            R.id.duration3 -> {
                uncheckDuration()
                if (duration == 6) {
                    binding.duration1.getBackground()
                        .setTint((getResources().getColor(R.color.brown_important_urgent_on)))
                    duration = 1
                } else {
                    binding.duration3.getBackground()
                        .setTint((getResources().getColor(R.color.brown_important_urgent_on)))
                    duration = 6
                }

            }

            R.id.duration4 -> {
                uncheckDuration()

                if (duration == 12) {
                    binding.duration1.getBackground()
                        .setTint((getResources().getColor(R.color.brown_important_urgent_on)))
                    duration = 1
                } else {
                    binding.duration4.getBackground()
                        .setTint((getResources().getColor(R.color.brown_important_urgent_on)))
                    duration = 12
                }

            }

            R.id.duration5 -> {
                uncheckDuration()
                if (duration == 24) {
                    binding.duration1.getBackground()
                        .setTint((getResources().getColor(R.color.brown_important_urgent_on)))
                    duration = 1
                } else {
                    binding.duration5.getBackground()
                        .setTint((getResources().getColor(R.color.brown_important_urgent_on)))
                    duration = 24
                }
            }

            R.id.duration6 -> {
                uncheckDuration()
                if (duration == 30) {
                    binding.duration1.getBackground()
                        .setTint((getResources().getColor(R.color.brown_important_urgent_on)))
                    duration = 1
                } else {
                    binding.duration6.getBackground()
                        .setTint((getResources().getColor(R.color.brown_important_urgent_on)))
                    duration = 30
                }
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
                var day: String = binding.tvDeadlineD.text.toString()
                if (day == "") {
                    day = today_day.toString()
                }

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

                val date_final = setUpDate(
                    day.toInt(),
                    month.toInt() - 1,
                    year.toInt()
                )
                //chosenItems.add(date_final)



                // Lista dat w stringach razem z datą wybraną powyżej
                var chosenDates: List<String> = markedDatePickerList.map {
                    val dateString = it.getTime()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .toString()
                    dateString
                } + date_final


                val chosenDatesMutable = chosenDates.toMutableList()
                val chosenItemsTemp = chosenDates.distinct()

                if (chosenDatesMutable.size != chosenItemsTemp.size) {
                    // List contains duplicates
                    chosenDatesMutable.removeLast()
                }

                chosenDates = chosenDatesMutable


                if (binding.taskTitle.text.toString().replace(" ", "") == "") {
                    val builder = AlertDialog.Builder(this)
                    val inflater = LayoutInflater.from(this)
                    val dialogView = inflater.inflate(R.layout.dialod_when_title_empty, null)
                    builder.setView(dialogView) //Podlaczanie xmla
                    val alertDialog = builder.create()
                    alertDialog.show()

                    dialogView.btn_ok.setOnClickListener {
                        alertDialog.cancel()
                    }

                } else {
                    for (date in chosenDates) {
                        val deadline = "$date " + setUpTime(
                            hour.toInt(),
                            minute.toInt()
                        )
                        val executionDate = if (specyficDate == 1) deadline else null

                        taskViewModel.insertTaskWithNote(
                            Tasks(
                                title = binding.taskTitle.text.toString(),
                                importance = important,
                                urgency = urgent,
                                deadline = deadline,
                                timeToFinish = duration,
                                isActive = 1,   //aktywny
                                typeId = type,
                                noteId = 0,
                                date = executionDate
                            ),
                            Notes(
                                noteTitle = binding.taskTitle.text.toString(),
                                noteContent = binding.note.text.toString(),
                                photo = null
                            )
                        )
                    }

                    //Toast.makeText(applicationContext, "record saved", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            R.id.btn_cancel -> {
                finish()
            }

            R.id.important0 -> {
                uncheckImportant()
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

            R.id.btn_deadline_time -> {
                val tpd = TimePickerDialog(
                    this,
                    R.style.MyDatePickerStyle,
                    { _, sel_hour, sel_minutes ->

                        time = setUpTime(sel_hour, sel_minutes)
                        setTimeBlocks(time)

                    }, today_hour, today_minute,
                    true
                )
                tpd.show()
            }

            R.id.btn_periodicity -> {

                val themeFactory = object : LightThemeFactory() {

                    override val pickedDayBackgroundShapeType: BackgroundShapeType
                        get() = BackgroundShapeType.ROUND_SQUARE

                    override val calendarViewPickedDayBackgroundColor: Int
                        get() = getColor(R.color.brown_important_urgent_on)

                    override val selectionBarMultipleDaysItemBackgroundColor: Int
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
                        get() = getColor(R.color.brown_important_urgent_off)


                }
                val callback = MultipleDaysPickCallback { days ->
                    markedDatePickerList = days
                }

                val datePicker = PrimeDatePicker.dialogWith(primeCalendar)
                    .pickMultipleDays(callback)
                    .initiallyPickedMultipleDays(markedDatePickerList)
                    .minPossibleDate(primeCalendar)
                    .firstDayOfWeek(Calendar.MONDAY)
                    .applyTheme(themeFactory)
                    .build()

                datePicker.show(supportFragmentManager, "AddingTaskRepeatPicker")
            }
        }
    }


    fun onClearRepeatsButtonClicked(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Czy chcesz usunąć wszystkie wybrane dni?")
        builder.setPositiveButton("Tak") { _, _ ->
            markedDatePickerList.clear()
        }
        builder.setNegativeButton("Nie") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
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

    fun setEverything()
    {
        binding.important0.setBackgroundColor(getResources().getColor(R.color.brown_important_urgent_on))
        binding.urgent0.setBackgroundColor(getResources().getColor(R.color.brown_important_urgent_on))

        binding.duration1.getBackground()
            .setTint((getResources().getColor(R.color.brown_important_urgent_on)))

    }

    fun uncheckDuration() {
        when (duration) {
            1 -> {
                binding.duration1.getBackground()
                    .setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }

            2 -> {
                binding.duration2.getBackground()
                    .setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }

            6 -> {
                binding.duration3.getBackground()
                    .setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }

            12 -> {
                binding.duration4.getBackground()
                    .setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }

            24 -> {
                binding.duration5.getBackground()
                    .setTint((getResources().getColor(R.color.brown_important_urgent_off)))

            }

            30 -> {
                binding.duration6.getBackground()
                    .setTint((getResources().getColor(R.color.brown_important_urgent_off)))
            }
        }
    }

    fun uncheckImportant() {
        when (important) {
            0 -> {
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
                binding.urgent0.setBackgroundColor(getResources().getColor(R.color.brown_important_urgent_off));
            }
            1 -> {
                binding.urgent1.setBackgroundColor(getResources().getColor(R.color.brown_important_urgent_off))
            }

        }
    }


}
