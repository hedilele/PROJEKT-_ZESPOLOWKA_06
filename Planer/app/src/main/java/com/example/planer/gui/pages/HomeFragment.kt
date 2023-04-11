package com.example.planer.gui.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planer.R
import com.example.planer.ViewModel.TaskViewModel
import com.example.planer.algorithm.BlockListTask
import com.example.planer.databinding.FragmentHomeBinding
import com.example.planer.entities.Tasks
import com.example.planer.gui.AdapterTasks
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

    // połączenie z xml
    private lateinit var binding: FragmentHomeBinding

    private lateinit var userViewModel: TaskViewModel

    // lista tasków do recyclerView
    var list = mutableListOf<Tasks>()


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
            {deleteId -> userViewModel.deleteTaskById(deleteId) },
            {updateTask, note -> userViewModel.updateTask(updateTask) }
        )
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(requireContext())



        val rv2 = view.tomorrow_task_list
        val adapter2 = AdapterTasks(
            list,
            {deleteId -> userViewModel.deleteTaskById(deleteId) },
            {updateTask, note -> userViewModel.updateTask(updateTask) }
        )
        rv2.adapter = adapter2
        rv2.layoutManager = LinearLayoutManager(requireContext())

        val rv3 = view.week_task_list
        val adapter3 = AdapterTasks(
            list,
            {deleteId -> userViewModel.deleteTaskById(deleteId) },
            {updateTask, note -> userViewModel.updateTask(updateTask) }
        )
        rv3.adapter = adapter3
        rv3.layoutManager = LinearLayoutManager(requireContext())

        val rv4 = view.month_task_list
        val adapter4 = AdapterTasks(
            list,
            {deleteId -> userViewModel.deleteTaskById(deleteId) },
            {updateTask, note -> userViewModel.updateTask(updateTask) }
        )
        rv4.adapter = adapter4
        rv4.layoutManager = LinearLayoutManager(requireContext())

        val rv5 = view.rest_task_list
        val adapter5 = AdapterTasks(
            list,
            {deleteId -> userViewModel.deleteTaskById(deleteId) },
            {updateTask, note -> userViewModel.updateTask(updateTask) }
        )
        rv5.adapter = adapter5
        rv5.layoutManager = LinearLayoutManager(requireContext())


        userViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        userViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            val blockListTask = BlockListTask(it)
            blockListTask.planner()
            adapter.setData(blockListTask.today_list)
            adapter2.setData(blockListTask.tomorrow_list)
            adapter3.setData(blockListTask.week_list)
            adapter4.setData(blockListTask.month_list)
            adapter5.setData(blockListTask.rest_list)
        })


        view.today_title.setOnClickListener {
            if(view.today_task_list.isVisible) view.today_task_list.visibility = View.GONE
            else view.today_task_list.visibility = View.VISIBLE
        }
        view.tomorrow_title.setOnClickListener {
            if(view.tomorrow_task_list.isVisible) view.tomorrow_task_list.visibility = View.GONE
            else view.tomorrow_task_list.visibility = View.VISIBLE
        }
        view.week_title.setOnClickListener {
            if(view.week_task_list.isVisible) view.week_task_list.visibility = View.GONE
            else view.week_task_list.visibility = View.VISIBLE
        }
        view.month_title.setOnClickListener {
            if(view.month_task_list.isVisible) view.month_task_list.visibility = View.GONE
            else view.month_task_list.visibility = View.VISIBLE
        }
        view.rest_title.setOnClickListener {
            if(view.rest_task_list.isVisible) view.rest_task_list.visibility = View.GONE
            else view.rest_task_list.visibility = View.VISIBLE
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



