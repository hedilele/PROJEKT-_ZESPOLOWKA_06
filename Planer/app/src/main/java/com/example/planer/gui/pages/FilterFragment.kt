package com.example.planer.gui.pages

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planer.R
import com.example.planer.ViewModel.TaskViewModel
import com.example.planer.algorithm.BlockListTask
import com.example.planer.entities.Tasks
import com.example.planer.gui.AdapterTasks
import kotlinx.android.synthetic.main.dialog_task_info.*
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.fragment_filter.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FilterFragment : Fragment(){
    //Podlaczanie xmla dialog_filter
    private lateinit var userViewModel: TaskViewModel
    // lista tasków do recyclerView
    var list = mutableListOf<Tasks>()
    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_filter, container, false)
        //Do wyszukiwania po nazwie
        val search = view.findViewById<AppCompatEditText>(R.id.search)
        val rv = view.rv_list
        val adapter = AdapterTasks(
            list,
            {deleteId -> userViewModel.deleteTaskById(deleteId) },
            {updateTask, note -> userViewModel.updateTask(updateTask) }
        )
        rv?.adapter = adapter
        rv?.layoutManager = LinearLayoutManager(requireContext())
        userViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        //Ustawiam flage, zeby mozna bylo po filtrowaniu nadasl wyszukiwac po naziwe
        search.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
                    //Pobieram to co jest w textview
                    val searchQuery = s.toString().trim()
                    CoroutineScope(Dispatchers.Main).launch {
                        userViewModel.readTasksWithSearchEdit(searchQuery)
                            .observe(viewLifecycleOwner, Observer {
                                val blockListTask = BlockListTask(it)
                                blockListTask.planner()
                                adapter.setData(
                                    (blockListTask.todayList + blockListTask.tomorrowList
                                            + blockListTask.weekList + blockListTask.monthList +
                                            blockListTask.restList).toMutableList()
                                )
                            })
                }

            }
            override fun afterTextChanged(p0: Editable?) {}
        })


        //Po kliknieciu na lejek ImageView
        view.searchView.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
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
            val startDay = dialogView.findViewById<EditText>(R.id.filter_date_start_d)
            val startMonth = dialogView.findViewById<EditText>(R.id.filter_date_start_m)
            val startYear = dialogView.findViewById<EditText>(R.id.filter_date_start_y)
            val endDay = dialogView.findViewById<EditText>(R.id.filter_date_end_d)
            val endMonth = dialogView.findViewById<EditText>(R.id.filter_date_end_m)
            val endYear = dialogView.findViewById<EditText>(R.id.filter_date_end_y)

            //Button do sortowania - pozniej moze byc czyms innym
            val sortButton = dialogView.findViewById<Button>(R.id.sortButton)

            //var typ: Int? = null
            //var durationn: Int? = null
            val typeIds = mutableListOf<Int>()
            val finishIds = mutableListOf<Int>()

            /*
            //TODO kiedy mam jakis textView i chce go zaznaczyc i odznaczyc
            fun uncheckDuration()
            {
                when(duration)
                {
                    duration-> {
                        duration1.getBackground().setTint(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_off))
                    }
                    2 -> {
                        duration2.getBackground().setTint(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_off))
                    }
                    3 -> {
                        duration3.getBackground().setTint(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_off))
                    }
                    4 -> {
                        duration4.getBackground().setTint(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_off))
                    }
                    5 -> {
                        duration5.getBackground().setTint(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_off))
                    }
                    6 -> {
                        duration6.getBackground().setTint(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_off))
                    }

                }

            }
             */

            //Ustawianie dla duration wyboru, jesli wybrany
            duration1.setOnClickListener{
                //uncheckDuration()
                duration1.getBackground().setTint(ContextCompat.getColor(requireContext(),R.color.brown_important_urgent_on))
                finishIds.add(1)
            }

            duration2.setOnClickListener{
                //uncheckDuration()
                duration2.getBackground().setTint(ContextCompat.getColor(requireContext(),R.color.brown_important_urgent_on))
                finishIds.add(2)
            }

            duration3.setOnClickListener{
                //uncheckDuration()
                duration3.getBackground().setTint(ContextCompat.getColor(requireContext(),R.color.brown_important_urgent_on))
                finishIds.add(6)
            }

            duration4.setOnClickListener{
                //uncheckDuration()
                duration4.getBackground().setTint(ContextCompat.getColor(requireContext(),R.color.brown_important_urgent_on))
                finishIds.add(12)
            }

            duration5.setOnClickListener{
                //uncheckDuration()
                duration5.getBackground().setTint(ContextCompat.getColor(requireContext(),R.color.brown_important_urgent_on))
                finishIds.add(24)
            }

            duration6.setOnClickListener{
                //uncheckDuration()
                duration6.getBackground().setTint(ContextCompat.getColor(requireContext(),R.color.brown_important_urgent_on))
                finishIds.add(30)
            }

            //Ustawianie dla typu koloru, jesli wybrany
            type1.setOnClickListener{
                type1.getBackground().setTint(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_on))
                typeIds.add(1)
            }

            type2.setOnClickListener{
                type2.getBackground().setTint(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_on))
                typeIds.add(2)
            }

            type3.setOnClickListener{
                type3.getBackground().setTint(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_on))
                typeIds.add(3)
            }

            type4.setOnClickListener{
                type4.getBackground().setTint(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_on))
                typeIds.add(4)
            }
            val alertDialog = builder.create()
            alertDialog.show()

            //Sortowanie po kliknieciu w przycisk
            sortButton.setOnClickListener{
                view.search.clearFocus()
                val rv = view.rv_list
                val adapter = AdapterTasks(
                    list,
                    {deleteId -> userViewModel.deleteTaskById(deleteId) },
                    {updateTask, note -> userViewModel.updateTask(updateTask) }
                )
                rv?.adapter = adapter
                rv?.layoutManager = LinearLayoutManager(requireContext())
                //Do parsowania daty

                userViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
                //Jesli jest pusto
                if(finishIds.isEmpty() && typeIds.isEmpty() && startDay.text.isBlank() && startMonth.text.isBlank()
                    && startYear.text.isBlank() && endDay.text.isBlank() && endMonth.text.isBlank()
                    && endYear.text.isBlank())
                {
                    userViewModel.readAllData.observe(viewLifecycleOwner, Observer {
                        val blockListTask = BlockListTask(it)
                        blockListTask.planner()
                        adapter.setData((blockListTask.todayList + blockListTask.tomorrowList
                                + blockListTask.weekList + blockListTask.monthList +
                                blockListTask.restList).toMutableList())
                        adapter.notifyDataSetChanged()
                    })
                }
                //Po typie
                if(typeIds.isNotEmpty())
                {
                    CoroutineScope(Dispatchers.Main).launch{
                        userViewModel.readTasksWithTypes(typeIds).observe(viewLifecycleOwner, Observer {
                            val blockListTask = BlockListTask(it)
                            blockListTask.planner()
                            adapter.setData((blockListTask.todayList + blockListTask.tomorrowList
                                    + blockListTask.weekList + blockListTask.monthList +
                                    blockListTask.restList).toMutableList())
                        })
                    }
                }
                //Po czasie trwania
                if(finishIds.isNotEmpty())
                {
                    CoroutineScope(Dispatchers.Main).launch{
                        userViewModel.readTasksWithDuration(finishIds).observe(viewLifecycleOwner, Observer {
                            val blockListTask = BlockListTask(it)
                            blockListTask.planner()
                            adapter.setData((blockListTask.todayList + blockListTask.tomorrowList
                                    + blockListTask.weekList + blockListTask.monthList +
                                    blockListTask.restList).toMutableList())
                        })
                    }
                }
                //Po dacie
                if(startDay.text.isNotBlank() && startMonth.text.isNotBlank() && startYear.text.isNotBlank() &&
                        endDay.text.isNotBlank() && endMonth.text.isNotBlank() && endYear.text.isNotBlank())
                {
                    val startDateString = "${startYear.text}-${startMonth.text}-${startDay.text}"
                    val endDateString = "${endYear.text}-${endMonth.text}-${endDay.text}"
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val startDate = dateFormat.parse(startDateString)?.time ?: 0
                    val endDate = dateFormat.parse(endDateString)?.time ?: 0
                    val startDateStringFormatted = dateFormat.format(startDate)
                    val endDateStringFormatted = dateFormat.format(endDate)
                    CoroutineScope(Dispatchers.Main).launch{
                        userViewModel.readTasksWithDate(startDateStringFormatted, endDateStringFormatted).observe(viewLifecycleOwner, Observer {
                            val blockListTask = BlockListTask(it)
                            blockListTask.planner()
                            adapter.setData((blockListTask.todayList + blockListTask.tomorrowList
                                    + blockListTask.weekList + blockListTask.monthList +
                                    blockListTask.restList).toMutableList())
                        })
                    }
                }
                //Po dacie i czasie trwania
                if((startDay.text.isNotBlank() && startMonth.text.isNotBlank() && startYear.text.isNotBlank() &&
                    endDay.text.isNotBlank() && endMonth.text.isNotBlank() && endYear.text.isNotBlank()) && finishIds.isNotEmpty())
                {
                    val startDateString = "${startYear.text}-${startMonth.text}-${startDay.text}"
                    val endDateString = "${endYear.text}-${endMonth.text}-${endDay.text}"
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val startDate = dateFormat.parse(startDateString)?.time ?: 0
                    val endDate = dateFormat.parse(endDateString)?.time ?: 0
                    val startDateStringFormatted = dateFormat.format(startDate)
                    val endDateStringFormatted = dateFormat.format(endDate)
                    CoroutineScope(Dispatchers.Main).launch{
                        userViewModel.readTasksWithDurationAndDate(finishIds,startDateStringFormatted, endDateStringFormatted).observe(viewLifecycleOwner, Observer {
                            val blockListTask = BlockListTask(it)
                            blockListTask.planner()
                            adapter.setData((blockListTask.todayList + blockListTask.tomorrowList
                                    + blockListTask.weekList + blockListTask.monthList +
                                    blockListTask.restList).toMutableList())
                        })
                    }
                }
                //Po dacie i typie
                if((startDay.text.isNotBlank() && startMonth.text.isNotBlank() && startYear.text.isNotBlank() &&
                            endDay.text.isNotBlank() && endMonth.text.isNotBlank() && endYear.text.isNotBlank()) && typeIds.isNotEmpty())
                {
                    val startDateString = "${startYear.text}-${startMonth.text}-${startDay.text}"
                    val endDateString = "${endYear.text}-${endMonth.text}-${endDay.text}"
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val startDate = dateFormat.parse(startDateString)?.time ?: 0
                    val endDate = dateFormat.parse(endDateString)?.time ?: 0
                    val startDateStringFormatted = dateFormat.format(startDate)
                    val endDateStringFormatted = dateFormat.format(endDate)
                    CoroutineScope(Dispatchers.Main).launch{
                        userViewModel.readTasksWithTypesAndDate(typeIds,startDateStringFormatted, endDateStringFormatted).observe(viewLifecycleOwner, Observer {
                            val blockListTask = BlockListTask(it)
                            blockListTask.planner()
                            adapter.setData((blockListTask.todayList + blockListTask.tomorrowList
                                    + blockListTask.weekList + blockListTask.monthList +
                                    blockListTask.restList).toMutableList())
                        })
                    }
                }
                //Po typie i czasie trwania
                if(typeIds.isNotEmpty() && finishIds.isNotEmpty())
                {
                    CoroutineScope(Dispatchers.Main).launch{
                        userViewModel.readTasksWithTypesAndDuration(typeIds,finishIds).observe(viewLifecycleOwner, Observer {
                            val blockListTask = BlockListTask(it)
                            blockListTask.planner()
                            adapter.setData((blockListTask.todayList + blockListTask.tomorrowList
                                    + blockListTask.weekList + blockListTask.monthList +
                                    blockListTask.restList).toMutableList())
                        })
                    }
                }
                //Po wszystkim
                if((startDay.text.isNotBlank() && startMonth.text.isNotBlank() && startYear.text.isNotBlank() &&
                            endDay.text.isNotBlank() && endMonth.text.isNotBlank() && endYear.text.isNotBlank()) && typeIds.isNotEmpty()
                    && finishIds.isNotEmpty())
                {
                    val startDateString = "${startYear.text}-${startMonth.text}-${startDay.text}"
                    val endDateString = "${endYear.text}-${endMonth.text}-${endDay.text}"
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val startDate = dateFormat.parse(startDateString)?.time ?: 0
                    val endDate = dateFormat.parse(endDateString)?.time ?: 0
                    val startDateStringFormatted = dateFormat.format(startDate)
                    val endDateStringFormatted = dateFormat.format(endDate)
                    CoroutineScope(Dispatchers.Main).launch{
                        userViewModel.readTasksWithTypesAndDurationAndDate(typeIds,finishIds,startDateStringFormatted, endDateStringFormatted)
                            .observe(viewLifecycleOwner, Observer {
                            val blockListTask = BlockListTask(it)
                            blockListTask.planner()
                            adapter.setData((blockListTask.todayList + blockListTask.tomorrowList
                                    + blockListTask.weekList + blockListTask.monthList +
                                    blockListTask.restList).toMutableList())
                        })
                    }
                }
                alertDialog.dismiss()
            }

        }

        //Ustawianie wyswietlania taskow w recycle view do listowania
        userViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            val blockListTask = BlockListTask(it)
            blockListTask.planner()
            adapter.setData((blockListTask.todayList + blockListTask.tomorrowList
                    + blockListTask.weekList + blockListTask.monthList +
                    blockListTask.restList).toMutableList())
        })
        return view
    }

}