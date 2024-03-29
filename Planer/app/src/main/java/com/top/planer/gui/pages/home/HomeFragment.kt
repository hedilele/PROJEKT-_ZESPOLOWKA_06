package com.top.planer.gui.pages.home

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.top.planer.R
import com.top.planer.ViewModel.*
import com.top.planer.algorithm.BlockListTask
import com.top.planer.databinding.FragmentHomeBinding
import com.top.planer.entities.Habits
import com.top.planer.entities.Notes
import com.top.planer.entities.Tasks
import com.top.planer.entities.Types
import com.top.planer.gui.pages.home.habits.AdapterHabits
import com.top.planer.gui.pages.home.tasks.AdapterTasks
import com.top.planer.gui.pages.scope.UnscrollableLinearLayoutManager
import kotlinx.android.synthetic.main.dialod_when_title_empty.view.*
import kotlinx.android.synthetic.main.dialog_habit.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.lang.Math.abs
import java.time.LocalDate

class HomeFragment : Fragment() {

    // połączenie z xml
    private lateinit var binding: FragmentHomeBinding

    private lateinit var userViewModel: TaskViewModel
    private val habitViewModel: HabitViewModel by viewModels()
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var typeViewModel: TypeViewModel

    private val settingViewModel: SettingsViewModel by viewModels()

    // lista tasków do recyclerView
    var list = mutableListOf<Tasks>()
    var listHab = mutableListOf<Habits>()
    var listNotes = mutableListOf<Notes>()
    var listTypes = mutableListOf<Types>()

    var delete_clicked = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        userViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        /*
        5 rv i adapterów do wyświetlania list tasków pobranych z bazy
         */
        val rv = view.today_task_list
        val adapter = AdapterTasks(
            childFragmentManager,
            list,
            listNotes,
            listTypes,
            { deleteTaskId, deleteNoteId ->
                userViewModel.deleteTaskAndNoteById(
                    deleteTaskId,
                    deleteNoteId
                )
            },
            { updateTask, updateNote -> userViewModel.updateTaskAndNote(updateTask, updateNote) },
        )
        rv.adapter = adapter
        rv.layoutManager = UnscrollableLinearLayoutManager(requireContext())

        val rv2 = view.tomorrow_task_list
        val adapter2 = AdapterTasks(
            childFragmentManager,
            list,
            listNotes,
            listTypes,
            { deleteTaskId, deleteNoteId ->
                userViewModel.deleteTaskAndNoteById(
                    deleteTaskId,
                    deleteNoteId
                )
            },
            { updateTask, updateNote -> userViewModel.updateTaskAndNote(updateTask, updateNote) })
        rv2.adapter = adapter2
        rv2.layoutManager = UnscrollableLinearLayoutManager(requireContext())

        val rv3 = view.week_task_list
        val adapter3 = AdapterTasks(
            childFragmentManager,
            list,
            listNotes,
            listTypes,
            { deleteTaskId, deleteNoteId ->
                userViewModel.deleteTaskAndNoteById(
                    deleteTaskId,
                    deleteNoteId
                )
            },
            { updateTask, updateNote ->
                userViewModel.updateTaskAndNote(updateTask, updateNote) })
        rv3.adapter = adapter3
        rv3.layoutManager = UnscrollableLinearLayoutManager(requireContext())

        val rv4 = view.month_task_list
        val adapter4 = AdapterTasks(
            childFragmentManager,
            list,
            listNotes,
            listTypes,
            { deleteTaskId, deleteNoteId ->
                userViewModel.deleteTaskAndNoteById(
                    deleteTaskId,
                    deleteNoteId
                )
            },
            { updateTask, updateNote -> userViewModel.updateTaskAndNote(updateTask, updateNote) })
        rv4.adapter = adapter4
        rv4.layoutManager = UnscrollableLinearLayoutManager(requireContext())

        val rv5 = view.rest_task_list
        val adapter5 = AdapterTasks(
            childFragmentManager,
            list,
            listNotes,
            listTypes,
            { deleteTaskId, deleteNoteId ->
                userViewModel.deleteTaskAndNoteById(
                    deleteTaskId,
                    deleteNoteId
                )
            },
            { updateTask, updateNote -> userViewModel.updateTaskAndNote(updateTask, updateNote) })
        rv5.adapter = adapter5
        rv5.layoutManager = UnscrollableLinearLayoutManager(requireContext())

        userViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        userViewModel.readAllData.observe(viewLifecycleOwner) { tasks ->
            settingViewModel.getHours().observe(viewLifecycleOwner) { h ->
                settingViewModel.getExcludedDates().observe(viewLifecycleOwner) { exDates ->
                    var hours: Int?
                    hours = h
                    if (hours == null)
                        hours = 5
                    val blockListTask = BlockListTask(tasks, requireContext(), hours, exDates)
                    blockListTask.planner()
                    adapter.updateList(blockListTask.todayList)
                    adapter2.updateList(blockListTask.tomorrowList)
                    adapter3.updateList(blockListTask.weekList)
                    adapter4.updateList(blockListTask.monthList)
                    adapter5.updateList(blockListTask.restList)

                    checkIfEmpty(adapter2.list, adapter3.list, adapter4.list, adapter5.list, view)

                }
            }
            adapter.notifyDataSetChanged()
            adapter2.notifyDataSetChanged()
            adapter3.notifyDataSetChanged()
            adapter4.notifyDataSetChanged()
            adapter5.notifyDataSetChanged()

        }

        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        noteViewModel.readAllData.observe(viewLifecycleOwner) {
            adapter.updateListOfNotes(it.toMutableList())
            adapter2.updateListOfNotes(it.toMutableList())
            adapter3.updateListOfNotes(it.toMutableList())
            adapter4.updateListOfNotes(it.toMutableList())
            adapter5.updateListOfNotes(it.toMutableList())


            adapter.notifyDataSetChanged()
            adapter2.notifyDataSetChanged()
            adapter3.notifyDataSetChanged()
            adapter4.notifyDataSetChanged()
            adapter5.notifyDataSetChanged()

        }

        typeViewModel = ViewModelProvider(this)[TypeViewModel::class.java]
        typeViewModel.readAllData.observe(viewLifecycleOwner) {
            adapter.updateListOfTypes(it.toMutableList())
            adapter2.updateListOfTypes(it.toMutableList())
            adapter3.updateListOfTypes(it.toMutableList())
            adapter4.updateListOfTypes(it.toMutableList())
            adapter5.updateListOfTypes(it.toMutableList())

            adapter.notifyDataSetChanged()
            adapter2.notifyDataSetChanged()
            adapter3.notifyDataSetChanged()
            adapter4.notifyDataSetChanged()
            adapter5.notifyDataSetChanged()

        }

        view.today_title.setOnClickListener {
            if (view.today_task_list.isVisible)
            {
                view.today_task_list.visibility = View.GONE
                view.today_list_arrow.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icon_expand_list))
                view.today_list_arrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_on), PorterDuff.Mode.SRC_ATOP)
            }
            else
            {
                view.today_task_list.visibility = View.VISIBLE
                view.today_list_arrow.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icon_wrap_list))
                view.today_list_arrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_on), PorterDuff.Mode.SRC_ATOP)
            }
        }
        view.tomorrow_title.setOnClickListener {
            if (view.tomorrow_task_list.isVisible)
            {
                view.tomorrow_task_list.visibility = View.GONE
                view.tomorrow_list_arrow.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icon_expand_list))
                view.tomorrow_list_arrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_on), PorterDuff.Mode.SRC_ATOP)
            }
            else
            {
                view.tomorrow_task_list.visibility = View.VISIBLE
                view.tomorrow_list_arrow.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icon_wrap_list))
                view.tomorrow_list_arrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_on), PorterDuff.Mode.SRC_ATOP)

            }
        }
        view.week_title.setOnClickListener {
            if (view.week_task_list.isVisible)
            {
                view.week_task_list.visibility = View.GONE
                view.week_list_arrow.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icon_expand_list))
                view.week_list_arrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_on), PorterDuff.Mode.SRC_ATOP)
            }
            else
            {
                view.week_task_list.visibility = View.VISIBLE
                view.week_list_arrow.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icon_wrap_list))
                view.week_list_arrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_on), PorterDuff.Mode.SRC_ATOP)
            }
        }
        view.month_title.setOnClickListener {
            if (view.month_task_list.isVisible)
            {
                view.month_task_list.visibility = View.GONE
                view.month_list_arrow.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icon_expand_list))
                view.month_list_arrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_on), PorterDuff.Mode.SRC_ATOP)
            }
            else
            {
                view.month_task_list.visibility = View.VISIBLE
                view.month_list_arrow.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icon_wrap_list))
                view.month_list_arrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_on), PorterDuff.Mode.SRC_ATOP)
            }
        }
        view.rest_title.setOnClickListener {
            if (view.rest_task_list.isVisible)
            {
                view.rest_task_list.visibility = View.GONE
                view.rest_list_arrow.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icon_expand_list))
                view.rest_list_arrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_on), PorterDuff.Mode.SRC_ATOP)
            }
            else
            {
                view.rest_task_list.visibility = View.VISIBLE
                view.rest_list_arrow.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icon_wrap_list))
                view.rest_list_arrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.brown_important_urgent_on), PorterDuff.Mode.SRC_ATOP)
            }
        }
        // HABITS

        habitViewModel.lastAccessDate.observe(viewLifecycleOwner) {
            if (it == null) {
                habitViewModel.createLastAccess()
                habitViewModel.activateHabits()
            } else if (it.lastAccessDate.isBefore(LocalDate.now())) {
                habitViewModel.activateHabits()
                habitViewModel.updateLastAccessDate()
            }
        }

        val rvh = view.habits_list
        val adapterhh = AdapterHabits(
            listHab,
            { hab ->
                if (delete_clicked == 1) {
                    habitViewModel.deleteHabit(hab)
                }
            },
            { hab ->
                if (delete_clicked == 0) {
                    habitViewModel.updateHabit(Habits(hab.id, hab.name, abs(hab.isActive - 1)))
                }
            }
        )

        rvh.adapter = adapterhh
        rvh.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        var allHabits = mutableListOf<Habits>()
        var activeHabits = mutableListOf<Habits>()
        var show_all_clicked = 0

        habitViewModel.readAllData.observe(viewLifecycleOwner) {

            allHabits = mutableListOf()
            activeHabits = mutableListOf()

            for (habit in it) {
                if (habit.isActive == 1) {
                    activeHabits.add(habit)
                }
            }

            adapterhh.updateList(activeHabits)
            allHabits = it.toMutableList()

            if(show_all_clicked == 1)
            {
                adapterhh.updateList(allHabits)
            }
            else
            {
                adapterhh.updateList(activeHabits)
            }

        }

        view.habits_add.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val dialogView = inflater.inflate(R.layout.dialog_habit, null)
            builder.setView(dialogView)

            val alertDialog = builder.create()
            alertDialog.show()

            dialogView.btn_create.setOnClickListener {

                if (dialogView.habit_title.text.toString().replace(" ", "") == "") {
                    val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    val inflater = LayoutInflater.from(requireContext())
                    val dialogView = inflater.inflate(R.layout.dialod_when_title_empty, null)
                    builder.setView(dialogView) //Podlaczanie xmla
                    val alertDialog = builder.create()
                    alertDialog.show()

                    dialogView.btn_ok.setOnClickListener {
                        alertDialog.cancel()
                    }
                } else {
                    habitViewModel.addHabit(
                        Habits(
                            name = dialogView.habit_title.text.toString(),
                            isActive = 1
                        )
                    )
                    alertDialog.cancel()
                }
            }
        }

        view.habits_delete.setOnClickListener {
            if (delete_clicked == 0) {
                view.habits_delete.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.pr1_delete_red
                    )
                )
                delete_clicked = 1
            } else {
                view.habits_delete.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.pr1_beige_background2
                    )
                )
                delete_clicked = 0
            }
        }

        view.habits_show_all.setOnClickListener {
            if (show_all_clicked == 0)
            {

                view.habits_show_all.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.brown_important_urgent_on
                    )
                )

                adapterhh.updateList(allHabits)

                show_all_clicked = 1
            } else {
                view.habits_show_all.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )

                adapterhh.updateList(activeHabits)

                show_all_clicked = 0
            }

        }
        return view
    }

    private fun hideRestOftasks(view: View?) {

        if (view?.today_task_list?.visibility == View.GONE)
            view.today_task_list?.visibility = View.VISIBLE
        if (view?.tomorrow_task_list?.visibility == View.VISIBLE)
            view.tomorrow_task_list?.visibility = View.GONE
        if (view?.week_task_list?.visibility == View.VISIBLE)
            view.week_task_list?.visibility = View.GONE
        if (view?.month_task_list?.visibility == View.VISIBLE)
            view.month_task_list?.visibility = View.GONE
        if (view?.rest_task_list?.visibility == View.VISIBLE)
            view.rest_task_list?.visibility = View.GONE

    }

    private fun checkIfEmpty(
        list2: MutableList<Tasks>,
        list3: MutableList<Tasks>,
        list4: MutableList<Tasks>,
        list5: MutableList<Tasks>,
        view: View
    ) {

        if (list2.size == 0) {
            view.tomorrow_task_list.visibility = View.GONE
            view.tomorrow_title.visibility = View.GONE
            view.line2.visibility = View.GONE
        } else {
            view.tomorrow_task_list.visibility = View.VISIBLE
            view.tomorrow_title.visibility = View.VISIBLE
            view.line2.visibility = View.VISIBLE
        }

        if (list3.size == 0) {
            view.week_task_list.visibility = View.GONE
            view.week_title.visibility = View.GONE
            view.line3.visibility = View.GONE
        } else {
            view.week_task_list.visibility = View.VISIBLE
            view.week_title.visibility = View.VISIBLE
            view.line3.visibility = View.VISIBLE

        }

        if (list4.size == 0) {
            view.month_task_list.visibility = View.GONE
            view.month_title.visibility = View.GONE
            view.line4.visibility = View.GONE

        } else {
            view.month_task_list.visibility = View.VISIBLE
            view.month_title.visibility = View.VISIBLE
            view.line4.visibility = View.VISIBLE

        }

        if (list5.size == 0) {
            view.rest_task_list.visibility = View.GONE
            view.rest_title.visibility = View.GONE
            view.line5.visibility = View.GONE

        } else {
            view.rest_task_list.visibility = View.VISIBLE
            view.rest_title.visibility = View.VISIBLE
            view.line5.visibility = View.VISIBLE

        }

    }

}



