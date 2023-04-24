package com.example.planer.gui.pages


/*
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planer.CalendarViewModel
import com.example.planer.R
import com.example.planer.algorithm.BlockListTask
import com.example.planer.databinding.ActivityCalendarBinding
import com.example.planer.entities.Calendar
import com.example.planer.gui.AddingEventActivity
import com.example.planer.gui.calendar.AdapterCalendar
import kotlinx.android.synthetic.main.activity_calendar.view.*


class CalendarFragment : Fragment() {
    private lateinit var binding: ActivityCalendarBinding

    private lateinit var calendarViewModel: CalendarViewModel

    // lista wydarzen do recyclerView
    var list = mutableListOf<Calendar>()

    // selected date
    private var selectedDate: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_calendar, container, false)

        // initialize selected date to current date
        selectedDate = System.currentTimeMillis()

        calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)

        val calview = view.calendarView
        val rv = view.list_rv
        val adapter = AdapterCalendar(
            list,
            { updateId -> },
            { deleteId -> },
        )
        rv?.adapter = adapter
        rv?.layoutManager = LinearLayoutManager(requireContext())

        calendarViewModel.getAll.observe(viewLifecycleOwner, Observer {
            adapter.updateList(it.toMutableList())
        })

        // set OnDateChangeListener to update selectedDate
        calview.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = java.util.Calendar.getInstance()
            calendar.set(year, month, dayOfMonth, 0, 0, 0)
            selectedDate = calendar.timeInMillis


        }

        view.add_event_to_calendar.setOnClickListener {
            activity?.let{
                val intent = Intent(it, AddingEventActivity::class.java)
                intent.putExtra("selected_date_millis", selectedDate)
                it.startActivity(intent)
            }
        }

        return view
    }
}
*/


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
//import com.applandeo.materialcalendarview.EventDay
//import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.example.planer.R
import com.example.planer.ViewModel.CalendarViewModel
import com.example.planer.databinding.ActivityCalendarBinding
import com.example.planer.entities.Calendar
import com.example.planer.gui.AdapterCalendar
import com.example.planer.gui.AddingEventActivity
import kotlinx.android.synthetic.main.activity_calendar.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class CalendarFragment : Fragment() {
    private lateinit var binding: ActivityCalendarBinding
    private lateinit var calendarViewModel: CalendarViewModel
    private var eventsList = mutableListOf<Calendar>()
    private var selectedDate: String = ""
    private var selectedDatel: Long = java.util.Calendar.getInstance().getTimeInMillis()




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_calendar, container, false)

        // initialize selected date to current date
        selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)

        val calview = view.calendarView
        val rv = view.list_rv
        val adapter = AdapterCalendar(
            eventsList,
            { updateId -> calendarViewModel.updateCalendar(updateId)},
            { deleteId -> calendarViewModel.deleteCalendarDateById(deleteId)},
        )
        rv?.adapter = adapter
        rv?.layoutManager = LinearLayoutManager(requireContext())

        // observe changes to eventsList and update adapter
        calendarViewModel.getAll.observe(viewLifecycleOwner, Observer {
            eventsList.clear()
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            for (event in it) {
                val eventDate = sdf.parse(event.startDate)?.let { sdf.format(it) } ?: ""
                if (eventDate.startsWith(selectedDate)) {
                    eventsList.add(event)

                }
            }
            adapter.notifyDataSetChanged()
        })

        /*  calview.setOnDayClickListener(object : OnDayClickListener {
              override fun onDayClick(eventDay: EventDay) {
                  val calendar: java.util.Calendar = eventDay.calendar
                  selectedDatel = calendar.timeInMillis
                  selectedDate =
                      SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

                  calendarViewModel.getAll.observe(viewLifecycleOwner, Observer {
                      eventsList.clear()
                      val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                      for (event in it) {
                          try {
                              val eventDate = sdf.parse(event.startDate)?.let { sdf.format(it) } ?: ""
                              if (eventDate.startsWith(selectedDate)) {
                                  eventsList.add(event)
                              }
                          } catch (e: ParseException) {
                              e.printStackTrace()
                          }
                      }
                      adapter.notifyDataSetChanged()
                  })
              }
          })
  */

        calview.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = java.util.Calendar.getInstance()
            calendar.set(year, month, dayOfMonth, 0, 0, 0)
            selectedDatel = calendar.timeInMillis
            selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            // update eventsList based on selected date
            calendarViewModel.getAll.value?.let { events ->
                eventsList.clear()
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                for (event in events) {
                    val eventDate = sdf.parse(event.startDate)?.let { sdf.format(it) } ?: ""
                    if (eventDate.startsWith(selectedDate)) {
                        eventsList.add(event)

                    }
                }
                adapter.notifyDataSetChanged()
            }
        }

        view.add_event_to_calendar.setOnClickListener {
            activity?.let{
                val intent = Intent(it, AddingEventActivity::class.java)
                intent.putExtra("selected_date_millis", selectedDatel)
                it.startActivity(intent)
            }
        }

        return view
    }
}

