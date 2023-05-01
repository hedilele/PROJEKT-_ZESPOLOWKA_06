package com.example.planer

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.planer.ViewModel.NoteViewModel
import com.example.planer.databinding.ActivityMainBinding
import com.example.planer.entities.Notes
import com.example.planer.gui.ViewPager2Adapter
import com.example.planer.gui.pages.home.tasks.AddingTaskActivity
import com.example.planer.gui.pages.*
import com.example.planer.gui.pages.filter.FilterFragment
import com.example.planer.gui.pages.home.HomeFragment
import com.example.planer.gui.pages.pomodoro.PomodoroActivity
import com.example.planer.gui.pages.pomodoro.PomodoroFragment
import com.example.planer.gui.pages.settings.UserSettingsActivity
import com.example.planer.scope.ScopeMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteViewModel: NoteViewModel

    lateinit var toggle: ActionBarDrawerToggle

    // private var pomodoroRecentlyOpened = false
//private lateinit var pagerAdapters: PagerAdapters

    var byDrawer = 0
    var pomodoroSeries: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val viewPager: ViewPager2 = binding.pagerView

        val fragments: ArrayList<Fragment> = arrayListOf(
            HomeFragment(),         // 0
            CalendarFragment(),     // 1
            FilterFragment()           //2
        )


        val adapter = ViewPager2Adapter(fragments, supportFragmentManager, lifecycle)
        viewPager.adapter = adapter


        // nawigacja - po kliknięciu na odpowiednią ikonę przenosi nas do danego fragmentu
        // strona główna (z recyclerView)
        binding.buttonHome.setOnClickListener {
            binding.pagerView.setCurrentItem(0)
        }

        // menu boczne - wysuwanie
        binding.buttonMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }

        // kalendarz
        binding.buttonCalendar.setOnClickListener {
            binding.pagerView.setCurrentItem(1)
        }

        // dodanie tasków - przekierowanie do nowej aktywności
        binding.buttonAdd.setOnClickListener {

            lifecycleScope.launch {
                val intent = Intent(applicationContext, AddingTaskActivity::class.java)
                startActivity(intent)
            }
        }

        //var pomodoroNote: Notes
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                var pomodoroNote: Notes? = noteViewModel.getNoteById(1)
                if (pomodoroNote?.noteContent != "pomodoro") {
                    noteViewModel.addNote(
                        Notes(
                            noteId = 1,
                            noteTitle = "pomodoro",
                            noteContent = "",
                            photo = null
                        )
                    )
                }
            }
        }

        // menu boczne - mechanizm odpowiedzialny za wysuwanie
        binding.apply {
            toggle = ActionBarDrawerToggle(
                this@MainActivity,
                drawerLayout,
                R.string.open,
                R.string.close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            binding?.navigation?.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.filter -> {

                        //val intent = Intent(applicationContext, FilterActivity::class.java)
                        //startActivity(intent)
                        pagerView.setCurrentItem(2)
//                        viewPager.isUserInputEnabled = false // disable swiping
                        binding.drawerLayout.closeDrawer(GravityCompat.START);

                    }

                    // pomodoro
                    R.id.pomodoro -> {

//                        if (isActivityRunningInForeground(applicationContext, PomodoroActivity::class.java)) {
//                            val intent = Intent(applicationContext, PomodoroActivity::class.java)
//                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
//                            startActivity(intent)
//                        }
//                        else
//                        {
                        val intent = Intent(applicationContext, PomodoroActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent)


                        //}

                        binding.drawerLayout.closeDrawer(GravityCompat.START);

                    }


                    R.id.settings -> {

                        val intent = Intent(applicationContext, UserSettingsActivity::class.java)
                        startActivity(intent)

                    }

                    R.id.scope_mode -> {

                        val intent = Intent(applicationContext, ScopeMode::class.java)
                        startActivity(intent)

                    }
                }
                true
            }
        }


    }
    //Do wylaczania trybu cichego kiedy ktos zamknie apkee
    override fun onDestroy() {
        super.onDestroy()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
    }
}

