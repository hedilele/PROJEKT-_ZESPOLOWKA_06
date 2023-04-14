package com.example.planer.gui.pages

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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
import kotlinx.android.synthetic.main.fragment_filter.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilterFragment : Fragment(){

    private var flaga = 0
    //Podlaczanie xmla dialog_filter
    private lateinit var userViewModel: TaskViewModel
    // lista tasków do recyclerView
    var list = mutableListOf<Tasks>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_filter, container, false)
        //Po kliknieciu na lejek ImageView
        //binding = FragmentFilterBinding.inflate(layoutInflater)
        view.searchView.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            //builder.setView(R.layout.activity_adding_task)

            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.dialog_filter, null)

            builder.setView(dialogView) //Podlaczanie xmla

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

            //Button do sortowania - pozniej moze byc czyms innym
            val sortButton = dialogView.findViewById<Button>(R.id.sortButton)

            //val duration : Int
            var typ: Int? = null
            var trwanie: Int? = null
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
                trwanie = 1;
            }

            duration2.setOnClickListener{
                //uncheckDuration()
                duration2.getBackground().setTint(ContextCompat.getColor(requireContext(),R.color.brown_important_urgent_on))
            }

            duration3.setOnClickListener{
                //uncheckDuration()
                duration3.getBackground().setTint(ContextCompat.getColor(requireContext(),R.color.brown_important_urgent_on))
            }

            duration4.setOnClickListener{
                //uncheckDuration()
                duration4.getBackground().setTint(ContextCompat.getColor(requireContext(),R.color.brown_important_urgent_on))
            }

            duration5.setOnClickListener{
                //uncheckDuration()
                duration5.getBackground().setTint(ContextCompat.getColor(requireContext(),R.color.brown_important_urgent_on))
            }

            duration6.setOnClickListener{
                //uncheckDuration()
                duration6.getBackground().setTint(ContextCompat.getColor(requireContext(),R.color.brown_important_urgent_on))
            }

            //Ustawianie dla typu koloru, jesli wybrany
            type1.setOnClickListener{
                type1.getBackground().setTint(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_on))
                typ = 0
            }

            type2.setOnClickListener{
                type2.getBackground().setTint(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_on))
                typ = 1
            }

            type3.setOnClickListener{
                type3.getBackground().setTint(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_on))
                typ = 2
            }

            type4.setOnClickListener{
                type4.getBackground().setTint(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_on))
                typ = 3
            }

            //TODO Dla wybierania daty

            val alertDialog = builder.create()
            alertDialog.show()

            //Sortowanie po kliknieciu w przycisk
            sortButton.setOnClickListener{
                val rv = view.rv_list
                val adapter = AdapterTasks(
                    list,
                    {deleteId -> userViewModel.deleteTaskById(deleteId) },
                    {updateTask, note -> userViewModel.updateTask(updateTask) }
                )
                rv?.adapter = adapter
                rv?.layoutManager = LinearLayoutManager(requireContext())

                userViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
                if(typ == 0 || typ == 1 || typ == 2 || typ == 3)
                {
                    println("**************************************")
                    println(typ)
                    CoroutineScope(Dispatchers.Main).launch{
                        userViewModel.readTasksWithTypes(typ!!).observe(viewLifecycleOwner, Observer {
                            val blockListTask = BlockListTask(it)
                            blockListTask.planner()
                            adapter.setData((blockListTask.todayList + blockListTask.tomorrowList
                                    + blockListTask.weekList + blockListTask.monthList +
                                    blockListTask.restList).toMutableList())
                        })
                    }
                }
                else if(typ == null)
                {
                    userViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
                    userViewModel.readAllData.observe(viewLifecycleOwner, Observer {
                        val blockListTask = BlockListTask(it)
                        blockListTask.planner()
                        adapter.setData((blockListTask.todayList + blockListTask.tomorrowList
                                + blockListTask.weekList + blockListTask.monthList +
                                blockListTask.restList).toMutableList())
                    })
                }
                alertDialog.dismiss()
            }

        }

        //Ustawianie wyswietlania taskow w recycle view do listowania
        val rv = view.rv_list
        val adapter = AdapterTasks(
            list,
            {deleteId -> userViewModel.deleteTaskById(deleteId) },
            {updateTask, note -> userViewModel.updateTask(updateTask) }
        )
        rv?.adapter = adapter
        rv?.layoutManager = LinearLayoutManager(requireContext())

        userViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
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