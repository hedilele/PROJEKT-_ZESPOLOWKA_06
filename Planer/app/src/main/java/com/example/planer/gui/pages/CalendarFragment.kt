package com.example.planer.gui.pages

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.ViewModel.CalendarViewModel
import com.example.planer.ViewModel.NoteViewModel
import com.example.planer.databinding.ActivityCalendarBinding
import com.example.planer.entities.Calendar
import com.example.planer.entities.Notes
import com.example.planer.gui.AdapterCalendarList
import com.example.planer.gui.AddingEventActivity
import com.example.planer.gui.CalendarAdapter
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.activity_calendar.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarFragment : Fragment(), CalendarAdapter.OnItemListener, CalendarAdapter.OnLongItemListener {
    private lateinit var binding: ActivityCalendarBinding
    private lateinit var calendarViewModel: CalendarViewModel
    private lateinit var notesViewModel: NoteViewModel
    private var eventsListoftheDay = mutableListOf<Calendar>() //klikniete eventy z danego dnia
    private var eventsListAll = mutableListOf<Calendar>() //wszystkie eventy

    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
    private var selectedDatenew: LocalDate? = null

    lateinit var adapter: AdapterCalendarList

    private var eventsOfMonthList = mutableListOf<Calendar>() //eventy do pokolorowania

    var notesList = mutableListOf<Notes>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_calendar, container, false)

        calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        notesViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        view.nextMonth.setOnClickListener{
            selectedDatenew = selectedDatenew!!.plusMonths(1).withDayOfMonth(1)

            setMonthView()
        }
        view.previousMonth.setOnClickListener {
            selectedDatenew = selectedDatenew!!.minusMonths(1).withDayOfMonth(1)
            setMonthView()
        }

        initWidgets(view)
        selectedDatenew = LocalDate.now()
        setMonthView()

        val rv = view.list_rv
        adapter = AdapterCalendarList(
            childFragmentManager,
            eventsListoftheDay,
            eventsListAll,
            notesList,
            { updateEvent,updateNote  ->
                calendarViewModel.updateCalendar(updateEvent)
                notesViewModel.updateNote(updateNote)

            },
            { deleteId, noteId ->
                calendarViewModel.deleteCalendarDateById(deleteId)
                notesViewModel.deleteNoteById(noteId)
            },
        )
        rv?.adapter = adapter
        rv?.layoutManager = LinearLayoutManager(requireContext())
        // observe changes to eventsList and update adapter
        calendarViewModel.getAll.observe(viewLifecycleOwner, Observer {
            eventsListoftheDay = mutableListOf()
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            for (event in it) {
                val eventDate = sdf.parse(event.startDate)?.let { sdf.format(it) } ?: ""
                if (eventDate.startsWith(selectedDatenew.toString())) {
                    eventsListoftheDay.add(event)
                }

            }
            adapter.updateList(eventsListoftheDay)
            adapter.updateList2(it.toMutableList())
            //calview.setEvents(eventswithicons)
            eventsListAll = it.toMutableList()

            setMonthView()
        })

        notesViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            adapter.updateListOfNotes(it.toMutableList())
            adapter.notifyDataSetChanged()
        })

        view.add_event_to_calendar.setOnClickListener {
            activity?.let{
                val intent = Intent(it, AddingEventActivity::class.java)
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

                var selectedDatenewinmilis = sdf.parse(selectedDatenew.toString()+" 12:00")?.time
                intent.putExtra("selected_date", selectedDatenewinmilis)
                it.startActivity(intent)
            }
        }


        return view
    }

    private fun monthYearFromDate(date: LocalDate?): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date!!.format(formatter)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(position: Int, dayText: String?) {
        if (dayText != "") {
            val message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDatenew)
            selectedDatenew = selectedDatenew?.withDayOfMonth(dayText!!.toInt())
            val yearMonth = YearMonth.from(selectedDatenew)
            var isNull = eventsListAll.filter {it.startDate.substring(0,7) ==yearMonth.toString() && it.startDate.substring(8,10).trimStart('0')== dayText}.toMutableList()
            if (!isNull.isNullOrEmpty()) {
                adapter.updateList(isNull)
            }

            else
            {
                adapter.updateList(mutableListOf<com.example.planer.entities.Calendar>())
            }
        }
        setMonthView()
    }
    @RequiresApi(Build.VERSION_CODES.O)

    override fun onLongItemClick(position: Int, dayText: String?){
        if (dayText!!.isEmpty())
        {
            return
        }
        else {
            activity?.let {
                val intent = Intent(it, AddingEventActivity::class.java)
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

                var selectedDatenewinmilis = sdf.parse(
                    selectedDatenew!!.withDayOfMonth(dayText!!.toInt()).toString() + " 12:00"
                )?.time
                intent.putExtra("selected_date", selectedDatenewinmilis)
                it.startActivity(intent)
            }
        }
    }

    private fun initWidgets(view : View) {
        calendarRecyclerView = view.findViewById<RecyclerView>(R.id.calendarRecyclerView)
        monthYearText = view.findViewById<TextView>(R.id.monthYearTV)
    }

    private fun setMonthView() {

        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.MONTH, selectedDatenew!!.monthValue-1)

        val sdf = SimpleDateFormat("LLLL", Locale("pl"))
        val monthName = sdf.format(calendar.time)

        monthYearText?.text = monthName + " "+ selectedDatenew!!.year.toString()
        val daysInMonth = daysInMonthArray(selectedDatenew)
        val yearMonth = YearMonth.from(selectedDatenew)

        eventsOfMonthList = eventsListAll.filter {it.startDate.substring(0,7) ==yearMonth.toString()}.toMutableList()


        val calendarAdapter = CalendarAdapter(daysInMonth, selectedDatenew.toString(), eventsOfMonthList,this, this)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 7)
        calendarRecyclerView!!.layoutManager = layoutManager
        calendarRecyclerView!!.adapter = calendarAdapter
    }

    private fun daysInMonthArray(date: LocalDate?): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = selectedDatenew!!.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value-1
        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }

        return daysInMonthArray
    }



}

