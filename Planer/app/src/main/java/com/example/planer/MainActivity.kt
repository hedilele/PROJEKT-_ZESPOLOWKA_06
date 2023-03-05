package com.example.planer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planer.DAOs.HabitsDAO
import com.example.planer.DAOs.TasksDAO
import com.example.planer.databinding.ActivityMainBinding
import com.example.planer.entities.Habits
import com.example.planer.entities.Tasks
import com.example.planer.gui.Adapter
import com.example.planer.gui.AddingTaskActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main)
        setContentView(binding?.root)

        val tasksDao = (application as DatabaseApp).db.tasksDAO()

        binding?.btnAdd?.setOnClickListener{
            //addHabit(habitsDao)
            val intent = Intent(this, AddingTaskActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch{
            tasksDao.fetchAll().collect{
                val list = ArrayList(it)
                setupListOfDataIntoRecyclerView(list, tasksDao)
            }
        }

    }

    private fun setupListOfDataIntoRecyclerView(taskList:ArrayList<Tasks>, tasksDAO: TasksDAO) {

        if(taskList.isNotEmpty()){
            val taskAdapter = Adapter(taskList,
                {updateId -> },
                {deleteId ->
                    lifecycleScope.launch{
                        tasksDAO.findTaskById(deleteId).collect{
                            if(it != null) {
                                deleteRecord(deleteId, tasksDAO, it)
                            }
                        }
                    }
                })

            binding?.taskList?.layoutManager = LinearLayoutManager(this)
            binding?.taskList?.adapter = taskAdapter
            binding?.taskList?.visibility = View.VISIBLE

        }
        else{
            binding?.taskList?.visibility = View.GONE
        }

    }

    fun deleteRecord(id:Int ,tasksDAO: TasksDAO, tasks: Tasks) {

        /*
            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("Delete Record")
            //set message for alert dialog
            builder.setMessage("Are you sure you wants to delete ${employee.name}.")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

         */
        //performing positive action
        // builder.setPositiveButton("Yes") { dialogInterface, _ ->
        lifecycleScope.launch {
            tasksDAO.delete(tasksDAO.findTaskById(id).first())


            //  dialogInterface.dismiss() // Dialog will be dismissed
        }

    }

}