package com.example.planer.gui.pages.filter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.example.planer.R
import com.example.planer.ViewModel.NoteViewModel
import com.example.planer.ViewModel.TaskViewModel
import com.example.planer.ViewModel.TypeViewModel
import com.example.planer.databinding.FragmentFilterBinding
import com.example.planer.entities.Notes
import com.example.planer.entities.Tasks
import com.example.planer.entities.Types
import com.example.planer.gui.pages.home.tasks.AdapterTasks
import kotlinx.android.synthetic.main.activity_adding_task.*
import kotlinx.android.synthetic.main.dialog_task_info.*
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.fragment_filter.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


/**
 * Klasa do listowania, zwraca pelna liste taskow i pozwala wylistowac taski roznymi kategorami
 * (typ, czas trwania, data), dodatkowo pozwala wyszukiwac taski po nazwie
 */
class FilterFragment : AppCompatActivity(){
    //Podlaczanie xmla dialog_filter
    private lateinit var userViewModel: TaskViewModel
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var typeViewModel: TypeViewModel

    // lista tasków do recyclerView
    var list = mutableListOf<Tasks>()
    private var listNotes = mutableListOf<Notes>()
    private var listTypes = mutableListOf<Types>()
    var filteredList = mutableListOf<Tasks>() //druga lista do wykorzystania
    private lateinit var binding: FragmentFilterBinding
    var duration: Int = 1
    var finishIds = mutableListOf<Int>()

    private val primeCalendar = CivilCalendar(TimeZone.getDefault(), Locale("pl", "PL"))


    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = FragmentFilterBinding.inflate(layoutInflater)

        // Set full screen
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(binding.root)

        val search = findViewById<AppCompatEditText>(R.id.search)

        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        userViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        typeViewModel = ViewModelProvider(this)[TypeViewModel::class.java]

        val rv = findViewById<RecyclerView>(R.id.rv_list)
        val adapter = AdapterTasks(
            list,
            listNotes,
            listTypes,
            { deleteTaskId, deleteNoteId -> userViewModel.deleteTaskAndNoteById(deleteTaskId, deleteNoteId) },
            { updateTask, updateNote -> userViewModel.updateTaskAndNote(updateTask, updateNote) }
        )
        rv?.adapter = adapter
        rv?.layoutManager = LinearLayoutManager(this)

        search.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
                //Pobieram to co jest w textview
                val searchQuery = s.toString().trim()
                if(filteredList.isEmpty())
                {
                    CoroutineScope(Dispatchers.Main).launch {
                        userViewModel.readTasksWithSearchEdit(searchQuery)
                            .observe(this@FilterFragment, Observer { //Tutaj zmiana kazdego na this@FilterFragment chyba
                                adapter.updateList(it.toMutableList())
                            })
                        adapter.notifyDataSetChanged()
                    }
                }
                else
                {
                    val filteredBySearchList = filteredList.filter {
                        it.title.contains(searchQuery, true)
                    }
                    adapter.updateList(filteredBySearchList.toMutableList())
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })


        //Po kliknieciu na lejek ImageView
        searchView.setOnClickListener{
            val builder = AlertDialog.Builder(this) //this
            val inflater = LayoutInflater.from(this)
            val dialogView = inflater.inflate(R.layout.dialog_filter, null)
            builder.setView(dialogView) //Podlaczanie xmla
            //Obsluga po wyjsciu z okna

            //TODO - przyciski i cala obsluga okna
            //TextView do ustawiania czasu trwania
            val duration1 = dialogView.findViewById<TextView>(R.id.duration1)
            val duration2 = dialogView.findViewById<TextView>(R.id.duration2)
            val duration3 = dialogView.findViewById<TextView>(R.id.duration3)
            val duration4 = dialogView.findViewById<TextView>(R.id.duration4)
            val duration5 = dialogView.findViewById<TextView>(R.id.duration5)
            val duration6 = dialogView.findViewById<TextView>(R.id.duration6)

            //TextView do ustawiania typow
            val type1 = dialogView.findViewById<TextView>(R.id.type1)
            val type2 = dialogView.findViewById<TextView>(R.id.type2)
            val type3 = dialogView.findViewById<TextView>(R.id.type3)
            val type4 = dialogView.findViewById<TextView>(R.id.type4)

            //Do daty
            val startDate = dialogView.findViewById<TextView>(R.id.filter_date_start)
            val endDate = dialogView.findViewById<TextView>(R.id.filter_date_end)

            val btn_start_date = dialogView.findViewById<ImageButton>(R.id.date_picker_start)
            val btn_end_date = dialogView.findViewById<ImageButton>(R.id.date_picker_end)

            //Button do sortowania - pozniej moze byc czyms innym
            val sortButton = dialogView.findViewById<Button>(R.id.sortButton)

            var typeIds = mutableListOf<Int>()
            finishIds = mutableListOf()
            typeIds = mutableListOf()

            fun setDateBlocks(date: String) : String
            {
                val table = date.split('-')
                return table[2] + " . " + table[1] + " . " + table[0]
            }

            btn_start_date.setOnClickListener {

                val callback = SingleDayPickCallback { day ->
                    val localDate: LocalDate = day.getTime()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    startDate.setText(setDateBlocks(localDate.toString()))
                    endDate.setText(setDateBlocks(localDate.plusYears(10).toString()))
                }

                val datePicker = PrimeDatePicker.dialogWith(primeCalendar)
                    .pickSingleDay(callback)

                    .initiallyPickedSingleDay(primeCalendar)
                    .minPossibleDate(primeCalendar)
                    .build()

                datePicker.show(supportFragmentManager, "AddingTaskDatePicker")
            }

            btn_end_date.setOnClickListener {

                val callback = SingleDayPickCallback { day ->
                    val localDate: LocalDate = day.getTime()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    endDate.setText(setDateBlocks(localDate.toString()))
                }

                val datePicker = PrimeDatePicker.dialogWith(primeCalendar)
                    .pickSingleDay(callback)

                    .initiallyPickedSingleDay(primeCalendar)
                    .minPossibleDate(primeCalendar)
                    .build()

                datePicker.show(supportFragmentManager, "AddingTaskDatePicker")

            }

            fun uncheckDuration(value :Int) {
                when (value) {
                    1 -> {
                        duration1.getBackground()
                            .setTint((getResources().getColor(R.color.brown_important_urgent_off)))

                        finishIds.removeAll { it == 1 }
                    }

                    2 -> {
                        duration2.getBackground()
                            .setTint((getResources().getColor(R.color.brown_important_urgent_off)))

                        finishIds.removeAll { it == 2 }
                    }

                    6 -> {
                        duration3.getBackground()
                            .setTint((getResources().getColor(R.color.brown_important_urgent_off)))

                        finishIds.removeAll { it == 6 }
                    }

                    12 -> {
                        duration4.getBackground()
                            .setTint((getResources().getColor(R.color.brown_important_urgent_off)))

                        finishIds.removeAll { it == 12}
                    }

                    24 -> {
                        duration5.getBackground()
                            .setTint((getResources().getColor(R.color.brown_important_urgent_off)))

                        finishIds.removeAll { it == 24 }
                    }

                    30 -> {
                        duration6.getBackground()
                            .setTint((getResources().getColor(R.color.brown_important_urgent_off)))

                        finishIds.removeAll { it == 30 }
                    }
                }
            }

            //Ustawianie dla duration wyboru, jesli wybrany
            duration1.setOnClickListener{

                if (finishIds.contains(1))  //is in the list
                {
                    uncheckDuration(1)
                }
                else
                {
                    duration1.background.setTint(ContextCompat.getColor(this,R.color.brown_important_urgent_on)) // kazdy requireContext() na this
                    finishIds.add(1)
                }
            }

            duration2.setOnClickListener{

                if (finishIds.contains(2))  //is in the list
                {
                    uncheckDuration(2)
                }
                else
                {
                    duration2.background.setTint(ContextCompat.getColor(this,R.color.brown_important_urgent_on))
                    finishIds.add(2)
                }

            }

            duration3.setOnClickListener{
                if (finishIds.contains(6))  //is in the list
                {
                    uncheckDuration(6)
                }
                else
                {
                    duration3.background.setTint(ContextCompat.getColor(this,R.color.brown_important_urgent_on))
                    finishIds.add(6)
                }

            }

            duration4.setOnClickListener{
                if (finishIds.contains(12))  //is in the list
                {
                    uncheckDuration(12)
                }
                else
                {
                    duration4.background.setTint(ContextCompat.getColor(this,R.color.brown_important_urgent_on))
                    finishIds.add(12)
                }

            }

            duration5.setOnClickListener{
                if (finishIds.contains(24))  //is in the list
                {
                    uncheckDuration(24)
                }
                else
                {
                    duration5.background.setTint(ContextCompat.getColor(this,R.color.brown_important_urgent_on))
                    finishIds.add(24)
                }

            }

            duration6.setOnClickListener{
                if (finishIds.contains(30))  //is in the list
                {
                    uncheckDuration(30)
                }
                else
                {
                    duration6.background.setTint(ContextCompat.getColor(this,R.color.brown_important_urgent_on))
                    finishIds.add(30)
                }

            }

            fun uncheckTypes(value :Int) {
                when (value) {
                    1 -> {
                        type1.getBackground()
                            .setTint((getResources().getColor(R.color.brown_important_urgent_off)))

                        typeIds.removeAll { it == 1 }
                    }

                    2 -> {
                        type2.getBackground()
                            .setTint((getResources().getColor(R.color.brown_important_urgent_off)))

                        typeIds.removeAll { it == 2 }
                    }

                    3 -> {
                        type3.getBackground()
                            .setTint((getResources().getColor(R.color.brown_important_urgent_off)))

                        typeIds.removeAll { it == 3 }
                    }

                    4 -> {
                        type4.getBackground()
                            .setTint((getResources().getColor(R.color.brown_important_urgent_off)))

                        typeIds.removeAll { it == 4 }
                    }

                }
            }

            typeViewModel.readAllData.observe(this) {
                type1.setText(it[0].name)
                type2.setText(it[1].name)
                type3.setText(it[2].name)
                type4.setText(it[3].name)
            }

            //Ustawianie dla typu koloru, jesli wybrany
            type1.setOnClickListener{

                if (typeIds.contains(1))  //is in the list
                {
                    uncheckTypes(1)
                }
                else
                {
                    type1.background.setTint(ContextCompat.getColor(this, R.color.brown_important_urgent_on))
                    typeIds.add(1)
                }

            }

            type2.setOnClickListener{
                if (typeIds.contains(2))  //is in the list
                {
                    uncheckTypes(2)
                }
                else
                {
                    type2.background.setTint(ContextCompat.getColor(this, R.color.brown_important_urgent_on))
                    typeIds.add(2)
                }

            }

            type3.setOnClickListener{
                if (typeIds.contains(3))  //is in the list
                {
                    uncheckTypes(3)
                }
                else
                {
                    type3.background.setTint(ContextCompat.getColor(this, R.color.brown_important_urgent_on))
                    typeIds.add(3)
                }

            }

            type4.setOnClickListener{
                if (typeIds.contains(4))  //is in the list
                {
                    uncheckTypes(4)
                }
                else
                {
                    type4.background.setTint(ContextCompat.getColor(this, R.color.brown_important_urgent_on))
                    typeIds.add(4)
                }

            }

            val alertDialog = builder.create()
            alertDialog.show()

            //Sortowanie po kliknieciu w przycisk
            sortButton.setOnClickListener{
                search.clearFocus()
                //Jesli jest pusto
                if(finishIds.isEmpty() && typeIds.isEmpty() && startDate.text.isBlank() && endDate.text.isBlank())
                {
                    CoroutineScope(Dispatchers.Main).launch {
                        userViewModel.readAllData.observe(this@FilterFragment, Observer {
                            filteredList = it.toMutableList()
                            adapter.updateList(filteredList)
                        })

                        noteViewModel.readAllData.observe(this@FilterFragment, Observer {
                            adapter.updateListOfNotes(it.toMutableList())
                        })

                        typeViewModel.readAllData.observe(this@FilterFragment, Observer {
                            adapter.updateListOfTypes(it.toMutableList())
                        })
                    }
                    checkText()
                }
                //Po typie
                if(typeIds.isNotEmpty())
                {
                    CoroutineScope(Dispatchers.Main).launch {
                        userViewModel.readTasksWithTypes(typeIds).observe(this@FilterFragment, Observer {
                            filteredList = it.toMutableList()
                            adapter.updateList(filteredList)
                        })

                    }
                    checkText()
                }
                //Po czasie trwania
                if(finishIds.isNotEmpty())
                {
                    //Kazdy viewLifecycleOwner na this@FilterFragment
                    CoroutineScope(Dispatchers.Main).launch {
                        userViewModel.readTasksWithDuration(finishIds).observe(this@FilterFragment, Observer {
                            filteredList = it.toMutableList()
                            adapter.updateList(filteredList)
                        })

                    }
                    checkText()
                }
                //Po dacie
                if(startDate.text.isNotBlank() && endDate.text.isNotBlank())
                {

                    val startDay = startDate.text.substring(0,2)
                    val startMonth = startDate.text.substring(5,7)
                    val startYear = startDate.text.substring(10)

                    val endDay = endDate.text.substring(0,2)
                    val endMonth = endDate.text.substring(5,7)
                    val endYear = endDate.text.substring(10)

                    val startDateString = "${startYear}-${startMonth}-${startDay}"
                    val endDateString = "${endYear}-${endMonth}-${endDay}"
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val startDate = dateFormat.parse(startDateString)?.time ?: 0
                    val endDate = dateFormat.parse(endDateString)?.time ?: 0
                    val startDateStringFormatted = dateFormat.format(startDate)
                    val endDateStringFormatted = dateFormat.format(endDate)
                    CoroutineScope(Dispatchers.Main).launch {
                        userViewModel.readTasksWithDate(startDateStringFormatted, endDateStringFormatted).observe(this@FilterFragment, Observer {
                            filteredList = it.toMutableList()
                            adapter.updateList(filteredList)
                        })

                    }
                    checkText()
                }
                //Po dacie i czasie trwania
                if(startDate.text.isNotBlank() && endDate.text.isNotBlank() && finishIds.isNotEmpty())
                {
                    val startDay = startDate.text.substring(0,2)
                    val startMonth = startDate.text.substring(5,7)
                    val startYear = startDate.text.substring(10)

                    val endDay = endDate.text.substring(0,2)
                    val endMonth = endDate.text.substring(5,7)
                    val endYear = endDate.text.substring(10)

                    val startDateString = "${startYear}-${startMonth}-${startDay}"
                    val endDateString = "${endYear}-${endMonth}-${endDay}"
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val startDate = dateFormat.parse(startDateString)?.time ?: 0
                    val endDate = dateFormat.parse(endDateString)?.time ?: 0
                    val startDateStringFormatted = dateFormat.format(startDate)
                    val endDateStringFormatted = dateFormat.format(endDate)
                    CoroutineScope(Dispatchers.Main).launch {
                        userViewModel.readTasksWithDurationAndDate(finishIds,startDateStringFormatted, endDateStringFormatted).observe(this@FilterFragment, Observer {
                            filteredList = it.toMutableList()
                            adapter.updateList(filteredList)
                        })

                    }
                    checkText()
                }
                //Po dacie i typie
                if(startDate.text.isNotBlank() && endDate.text.isNotBlank() && typeIds.isNotEmpty())
                {
                    val startDay = startDate.text.substring(0,2)
                    val startMonth = startDate.text.substring(5,7)
                    val startYear = startDate.text.substring(10)

                    val endDay = endDate.text.substring(0,2)
                    val endMonth = endDate.text.substring(5,7)
                    val endYear = endDate.text.substring(10)

                    val startDateString = "${startYear}-${startMonth}-${startDay}"
                    val endDateString = "${endYear}-${endMonth}-${endDay}"
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val startDate = dateFormat.parse(startDateString)?.time ?: 0
                    val endDate = dateFormat.parse(endDateString)?.time ?: 0
                    val startDateStringFormatted = dateFormat.format(startDate)
                    val endDateStringFormatted = dateFormat.format(endDate)
                    CoroutineScope(Dispatchers.Main).launch {
                        userViewModel.readTasksWithTypesAndDate(typeIds,startDateStringFormatted, endDateStringFormatted).observe(this@FilterFragment, Observer {
                            filteredList = it.toMutableList()
                            adapter.updateList(filteredList)
                        })

                    }
                    checkText()
                }
                //Po typie i czasie trwania
                if(typeIds.isNotEmpty() && finishIds.isNotEmpty())
                {
                    CoroutineScope(Dispatchers.Main).launch {
                        userViewModel.readTasksWithTypesAndDuration(typeIds,finishIds).observe(this@FilterFragment, Observer {
                            filteredList = it.toMutableList()
                            adapter.updateList(filteredList)
                        })

                    }
                    checkText()
                }
                //Po wszystkim
                if(startDate.text.isNotBlank() && endDate.text.isNotBlank() && typeIds.isNotEmpty() && finishIds.isNotEmpty())
                {
                    val startDay = startDate.text.substring(0,2)
                    val startMonth = startDate.text.substring(5,7)
                    val startYear = startDate.text.substring(10)

                    val endDay = endDate.text.substring(0,2)
                    val endMonth = endDate.text.substring(5,7)
                    val endYear = endDate.text.substring(10)

                    val startDateString = "${startYear}-${startMonth}-${startDay}"
                    val endDateString = "${endYear}-${endMonth}-${endDay}"
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val startDate = dateFormat.parse(startDateString)?.time ?: 0
                    val endDate = dateFormat.parse(endDateString)?.time ?: 0
                    val startDateStringFormatted = dateFormat.format(startDate)
                    val endDateStringFormatted = dateFormat.format(endDate)
                    CoroutineScope(Dispatchers.Main).launch {
                        userViewModel.readTasksWithTypesAndDurationAndDate(typeIds,finishIds,startDateStringFormatted, endDateStringFormatted).observe(this@FilterFragment, Observer {
                            filteredList = it.toMutableList()
                            adapter.updateList(filteredList)
                        })

                    }
                    checkText()
                }
                alertDialog.dismiss()
                search.requestFocus()
            }

        }
        //Ustawianie wyswietlania taskow w recycle view do listowania
        userViewModel.readAllData.observe(this@FilterFragment, Observer {
            adapter.updateList(it.toMutableList())
        })

        noteViewModel.readAllData.observe(this@FilterFragment, Observer {
            adapter.updateListOfNotes(it.toMutableList())
        })


        typeViewModel.readAllData.observe(this@FilterFragment, Observer {
            adapter.updateListOfTypes(it.toMutableList())
        })



        binding.ivBackToMenu.setOnClickListener {
            finish()
        }

    }
    //Sprawdzam czy editText jest pusty, jesli nie to usuwam
    private fun checkText()
    {
        if(search.text?.isNotBlank() == true)
        {
            search.setText("")
        }
    }
}