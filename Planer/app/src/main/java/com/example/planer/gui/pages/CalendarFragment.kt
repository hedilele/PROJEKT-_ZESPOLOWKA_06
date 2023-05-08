package com.example.planer.gui.pages

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
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


//class CalendarFragment : Fragment() {
class CalendarFragment : Fragment() {
    private lateinit var binding: ActivityCalendarBinding
    private lateinit var calendarViewModel: CalendarViewModel
    private var eventsList = mutableListOf<Calendar>() //klikniete eventy
    private var eventsList2 = mutableListOf<Calendar>() //wszystkie eventy
    private var eventswithicons = mutableListOf<EventDay>() //wszystkie eventy (do wyświetlania ikon)
    private var selectedDate: String = ""
    private var selectedDatel: Long = java.util.Calendar.getInstance().getTimeInMillis()





    private lateinit var mViewPager: ViewPager2


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
        // calview.isNestedScrollingEnabled = true


        val rv = view.list_rv
        val adapter = AdapterCalendar(
            eventsList,eventsList2,
            { updateId -> calendarViewModel.updateCalendar(updateId)},
            { deleteId -> calendarViewModel.deleteCalendarDateById(deleteId)},
        )
        rv?.adapter = adapter
        rv?.layoutManager = LinearLayoutManager(requireContext())

        // observe changes to eventsList and update adapter
        calendarViewModel.getAll.observe(viewLifecycleOwner, Observer {
            eventsList.clear()
            eventswithicons.clear()
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            for (event in it) {
                val eventDate = sdf.parse(event.startDate)?.let { sdf.format(it) } ?: ""
                if (eventDate.startsWith(selectedDate)) {
                    eventsList.add(event)

                }
                var calendar1 = java.util.Calendar.getInstance()
                calendar1.set(event.startDate.substring(0,4).toInt(),event.startDate.substring(5,7).toInt()-1,event.startDate.substring(8, 10).toInt())

                eventswithicons.add(EventDay(calendar1, R.drawable.iconmoon))

            }
            adapter.updateList(eventsList)
            adapter.updateList2(it.toMutableList())
            calview.setEvents(eventswithicons)



        })

        // fun onTouch(View v, MotionEvent e) {
        // if(e.getAction() == MotionEvent.ACTION_DOWN){
        calview.getParent().requestDisallowInterceptTouchEvent(true)
        //  }

        calview.setOnPreviousPageChangeListener(object : OnCalendarPageChangeListener {
            override fun onChange() {

                eventsList.clear()
                adapter.updateList(eventsList)
                adapter.notifyDataSetChanged()


            }
        })



        calview.setOnForwardPageChangeListener(object : OnCalendarPageChangeListener {
            override fun onChange() {
                eventsList.clear()
                adapter.updateList(eventsList)
                adapter.notifyDataSetChanged()


            }
        })









        calview.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val calendar: java.util.Calendar = eventDay.calendar
                selectedDatel = calendar.timeInMillis
                selectedDate =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                //calview.setDate(calendar)
                var callist = mutableListOf<java.util.Calendar>()
                callist.add(calendar)





                calview.setHighlightedDays(callist)

                //  calview.setCalendarDays(callist)


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
                    // adapter.updateList(eventsList)
                })
            }
        })



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

