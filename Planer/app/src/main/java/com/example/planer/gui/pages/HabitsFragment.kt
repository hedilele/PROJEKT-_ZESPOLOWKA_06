package com.example.planer.gui.pages

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planer.R
import com.example.planer.databinding.FragmentHabitsBinding
import com.example.planer.entities.Habits
import com.example.planer.gui.AdapterHabits
import com.example.planer.gui.AddingHabitActivity
import com.example.planer.gui.AddingTaskActivity
import kotlinx.android.synthetic.main.fragment_home.view.*


class HabitsFragment : Fragment() {


    private lateinit var binding: FragmentHabitsBinding
    //private lateinit var userViewModel: Habits


    var list = mutableListOf<Habits>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_habits, container, false)

        binding = FragmentHabitsBinding.inflate(layoutInflater)
        //userViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)


        list.add(Habits(1,"",1))
        list.add(Habits(2,"",1))
        list.add(Habits(3,"",1))
        list.add(Habits(4,"",1))
        list.add(Habits(5,"",1))
        list.add(Habits(6,"",1))
        list.add(Habits(7,"",1))
        list.add(Habits(8,"",1))
        list.add(Habits(9,"",1))

        val rv = view.today_task_list
        val adapter = AdapterHabits(
            list,
            {deleteId ->  },
            {updateTask -> }
        )
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(requireContext())


        binding.btnAdd.setOnClickListener {
           val intent = Intent(this.activity, AddingHabitActivity::class.java)
           startActivity(intent)
        }


        return view
    }


}