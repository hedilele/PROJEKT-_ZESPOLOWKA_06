package com.top.planer

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.top.planer.ViewModel.CalendarViewModel
import com.top.planer.ViewModel.NoteViewModel
import com.top.planer.databinding.ActivityMainBinding
import com.top.planer.entities.Calendar
import com.top.planer.entities.Notes
import com.top.planer.gui.ViewPager2Adapter
import com.top.planer.gui.pages.home.tasks.AddingTaskActivity
import com.top.planer.gui.pages.*
import com.top.planer.gui.pages.filter.FilterFragment
import com.top.planer.gui.pages.guide.GuideActivity
import com.top.planer.gui.pages.home.HomeFragment
import com.top.planer.gui.pages.home.notes.NotesActivity
import com.top.planer.gui.pages.pomodoro.PomodoroActivity
import com.top.planer.gui.pages.settings.UserSettingsActivity
import com.top.planer.notification.NotificationHelper
import com.top.planer.notification.NotificationService
import com.top.planer.gui.pages.scope.ScopeMode
import kotlinx.android.synthetic.main.dialod_when_title_empty.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var calendarViewModel: CalendarViewModel

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Set full screen
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(binding.root)
        //DO POWIADOMIEŃ
        calendarViewModel = ViewModelProvider(this)[CalendarViewModel::class.java]

        calendarViewModel.getAll.observe(this) { calendars ->
            scheduleNotifications(calendars)
        }
        val intent = Intent(applicationContext, NotificationService::class.java)
        startService(intent)

        val viewPager: ViewPager2 = binding.pagerView

        val fragments: ArrayList<Fragment> = arrayListOf(
            HomeFragment(),         // 0
            CalendarFragment(),     // 1
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

        binding.buttonAdd.setOnLongClickListener{

            val builder = AlertDialog.Builder(this)
            val inflater = LayoutInflater.from(this)
            val dialogView = inflater.inflate(R.layout.dialog_short_note, null)
            builder.setView(dialogView) //Podlaczanie xmla

            val content = dialogView.findViewById<AppCompatEditText>(R.id.note_content)
            val btn_edit = dialogView.findViewById<Button>(R.id.btn_edit)
            btn_edit.setText("Stwórz")

            val alertDialog = builder.create()
            alertDialog.show()

            content.requestFocus()
            val window: Window? = alertDialog.getWindow()
            window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

            btn_edit.setOnClickListener{

                if(content.text.toString().replace(" ", "") == "")
                {
                    val builder = AlertDialog.Builder(this)
                    val inflater = LayoutInflater.from(this)
                    val dialogView = inflater.inflate(R.layout.dialod_when_title_empty, null)
                    builder.setView(dialogView) //Podlaczanie xmla
                    val alertDialog = builder.create()
                    alertDialog.show()

                    dialogView.btn_ok.setOnClickListener {
                        alertDialog.cancel()
                    }

                }
                else
                {
                    noteViewModel.addNote(Notes(noteTitle = "0short", noteContent = content.text.toString(), photo = null))
                    alertDialog.cancel()
                }

            }
            true
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

                        val intent = Intent(applicationContext, FilterFragment::class.java)
                        startActivity(intent)
                        binding.drawerLayout.closeDrawer(GravityCompat.START);

                    }
                    // pomodoro
                    R.id.pomodoro -> {
                        val intent = Intent(applicationContext, PomodoroActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent)

                        binding.drawerLayout.closeDrawer(GravityCompat.START);

                    }

                    R.id.settings -> {

                        val intent = Intent(applicationContext, UserSettingsActivity::class.java)
                        startActivity(intent)
                        binding.drawerLayout.closeDrawer(GravityCompat.START);

                    }

                    R.id.scope_mode -> {

                        val intent = Intent(applicationContext, ScopeMode::class.java)
                        startActivity(intent)
                        binding.drawerLayout.closeDrawer(GravityCompat.START);

                    }

                    R.id.short_notes -> {
                        val intent = Intent(applicationContext, NotesActivity::class.java)
                        startActivity(intent)
                        binding.drawerLayout.closeDrawer(GravityCompat.START);

                    }

                    R.id.guide -> {
                        val intent = Intent(applicationContext, GuideActivity::class.java)
                        startActivity(intent)
                        binding.drawerLayout.closeDrawer(GravityCompat.START);

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
        //Todo wywala na emulatorze
        //notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
    }

    override fun onResume() {
        super.onResume()
    }

    //Metody do pobierania powiadomien
    private fun scheduleNotifications(calendars: List<Calendar>) //tasks: List<Tasks>
    {
        // Sprawdzenie, czy lista zadań nie jest pusta
        if(!calendars.isNullOrEmpty())
        {
            for(calendar in calendars)
            {
                scheduleNotification(calendar.startDate,calendar.id,calendar.reminder,calendar.name) //task.deadline, task.id, task.title
            }
        }
    }

    private fun scheduleNotification(startDate: String, calendarId: Long, reminder: Int,name: String)
    {
        val notificationHelper = NotificationHelper(this)
        notificationHelper.scheduleNotification(startDate, calendarId,reminder, name)
    }
}

