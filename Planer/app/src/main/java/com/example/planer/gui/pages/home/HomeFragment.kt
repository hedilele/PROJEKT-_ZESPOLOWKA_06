package com.example.planer.gui.pages.home

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planer.R
import com.example.planer.ViewModel.HabitViewModel
import com.example.planer.ViewModel.TaskViewModel
import com.example.planer.algorithm.BlockListTask
import com.example.planer.databinding.FragmentHomeBinding
import com.example.planer.entities.Habits
import com.example.planer.entities.Tasks
import com.example.planer.gui.pages.home.habits.AdapterHabits
import com.example.planer.gui.pages.home.tasks.AdapterTasks
import kotlinx.android.synthetic.main.dialog_habit.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

    // połączenie z xml
    private lateinit var binding: FragmentHomeBinding

    private lateinit var userViewModel: TaskViewModel
    private lateinit var habitViewModel: HabitViewModel

    private lateinit var adapterhh: AdapterHabits




    // lista tasków do recyclerView
    var list = mutableListOf<Tasks>()
    var listHab = mutableListOf<Habits>()

    var delete_clicked = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.inflate(layoutInflater)

        userViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        //TODO - podlaczanie taskow pod wyswietlanie
        /*
        5 rv i adapterów do wyświetlania list tasków pobranych z bazy
         */
        val rv = view.today_task_list
        val adapter = AdapterTasks(
            list,
            { deleteId -> userViewModel.deleteTaskById(deleteId) },
            { updateTask, note -> userViewModel.updateTask(updateTask) }
        )
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(requireContext())


        val rv2 = view.tomorrow_task_list
        val adapter2 = AdapterTasks(
            list,
            { deleteId -> userViewModel.deleteTaskById(deleteId) },
            { updateTask, note -> userViewModel.updateTask(updateTask) }
        )
        rv2.adapter = adapter2
        rv2.layoutManager = LinearLayoutManager(requireContext())

        val rv3 = view.week_task_list
        val adapter3 = AdapterTasks(
            list,
            { deleteId -> userViewModel.deleteTaskById(deleteId) },
            { updateTask, note -> userViewModel.updateTask(updateTask) }
        )
        rv3.adapter = adapter3
        rv3.layoutManager = LinearLayoutManager(requireContext())

        val rv4 = view.month_task_list
        val adapter4 = AdapterTasks(
            list,
            { deleteId -> userViewModel.deleteTaskById(deleteId) },
            { updateTask, note -> userViewModel.updateTask(updateTask) }
        )
        rv4.adapter = adapter4
        rv4.layoutManager = LinearLayoutManager(requireContext())

        val rv5 = view.rest_task_list
        val adapter5 = AdapterTasks(
            list,
            { deleteId -> userViewModel.deleteTaskById(deleteId) },
            { updateTask, note -> userViewModel.updateTask(updateTask) }
        )
        rv5.adapter = adapter5
        rv5.layoutManager = LinearLayoutManager(requireContext())

        //val io = IO()
        //io.newDay(requireContext())


        userViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        userViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            val blockListTask = BlockListTask(it, 60)
            blockListTask.planner()
            adapter.setData(blockListTask.todayList)
            adapter2.setData(blockListTask.tomorrowList)
            adapter3.setData(blockListTask.weekList)
            adapter4.setData(blockListTask.monthList)
            adapter5.setData(blockListTask.restList)
        })


        view.today_title.setOnClickListener {
            if (view.today_task_list.isVisible) view.today_task_list.visibility = View.GONE
            else view.today_task_list.visibility = View.VISIBLE
        }
        view.tomorrow_title.setOnClickListener {
            if (view.tomorrow_task_list.isVisible) view.tomorrow_task_list.visibility = View.GONE
            else view.tomorrow_task_list.visibility = View.VISIBLE
        }
        view.week_title.setOnClickListener {
            if (view.week_task_list.isVisible) view.week_task_list.visibility = View.GONE
            else view.week_task_list.visibility = View.VISIBLE
        }
        view.month_title.setOnClickListener {
            if (view.month_task_list.isVisible) view.month_task_list.visibility = View.GONE
            else view.month_task_list.visibility = View.VISIBLE
        }
        view.rest_title.setOnClickListener {
            if (view.rest_task_list.isVisible) view.rest_task_list.visibility = View.GONE
            else view.rest_task_list.visibility = View.VISIBLE
        }




        //habits

        var habit_clicked = 0
        var habitid = 0

        val rvh = view.habits_list
        val adapterhh = AdapterHabits(
            listHab,
            {hab -> if(delete_clicked == 1) habitViewModel.deleteHabit(hab)},
            {hab -> if(delete_clicked == 0) habitViewModel.updateHabit(Habits(hab.id, hab.name, 0))}
        )

        rvh.adapter = adapterhh
        rvh.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        habitViewModel = ViewModelProvider(this).get(HabitViewModel::class.java)

        //habitViewModel = ViewModelProvider(this)[HabitViewModel::class.java]
        habitViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            adapterhh.updateList(it.toMutableList())
        })


        Toast.makeText(requireContext(), listHab.size.toString(), Toast.LENGTH_SHORT).show()



        view.habits_add.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val dialogView = inflater.inflate(R.layout.dialog_habit, null)
            builder.setView(dialogView)

            val alertDialog = builder.create()
            alertDialog.show()

            dialogView.btn_create.setOnClickListener {
                if (dialogView.habit_title.text != null) {
                    //listhab.add(Habits(name = dialogView.habit_title.text.toString(), isActive = 1))
                    habitViewModel.addHabit(
                        Habits(
                            name = dialogView.habit_title.text.toString(),
                            isActive = 1
                        )
                    )
                }
                alertDialog.cancel()
            }

        }

        view.habits_delete.setOnClickListener {
            if (delete_clicked == 0) {
                view.habits_delete.setBackgroundColor(Color.parseColor("#33832508"))
                delete_clicked = 1
            } else {
                view.habits_delete.setBackgroundColor(Color.parseColor("#99F3DEBA"))
                delete_clicked = 0
            }


        }



        return view

    }


}



// może się przydać, więc nie usuwam

//    private fun setupListOfDataIntoRecyclerView(taskList:ArrayList<Tasks>, tasksDAO: TasksDAO, view: View) {
//
//        if(taskList.isNotEmpty()){
//
//            var mRecyclerView = view.findViewById<View>(R.id.task_list) as RecyclerView
//            var mLayoutManager = LinearLayoutManager(this.activity)
//            mRecyclerView.setLayoutManager(mLayoutManager)
//
//            var mAdapter = Adapter(list,
//                {updateId -> },
//                {deleteId ->
//                    lifecycleScope.launch{
//                        tasksDAO.findTaskById(deleteId).collect{
//                            if(it != null) {
//                                deleteRecord(deleteId, tasksDAO, it)
//                            }
//                        }
//                    }})
//
//            mRecyclerView.setAdapter(mAdapter)
//
//        }
//        else{
//            Toast.makeText(this.context, "JU", Toast.LENGTH_SHORT)
//        }
//
//    }
//
//    fun deleteRecord(id:Int ,tasksDAO: TasksDAO, tasks: Tasks) {
//
//        lifecycleScope.launch {
//            tasksDAO.delete(tasksDAO.findTaskById(id).first())
//        }
//
//    }



