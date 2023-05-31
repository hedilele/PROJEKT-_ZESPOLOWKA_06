package com.top.planer.gui.pages.pomodoro

import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.*
import android.provider.Settings
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.top.planer.MainActivity
import com.top.planer.R
import com.top.planer.ViewModel.NoteViewModel
import com.top.planer.databinding.ActivityPomodoroBinding
import com.top.planer.entities.Notes
import kotlinx.android.synthetic.main.dialog_note_pomodoro.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PomodoroActivity : AppCompatActivity() {

    lateinit var binding: ActivityPomodoroBinding

    private var time_selected: Int = 0
    private var timer: CountDownTimer? = null
    private var time_progress = 0
    private var left_time: Long = 0
    private var ready_to_start = true
    private var isEnabled = false

    lateinit var progressBar: ProgressBar
    lateinit var startBtn: Button
    lateinit var resetBtn: Button
    lateinit var timeLeftTv: TextView
    lateinit var work_break: TextView

    private lateinit var noteViewModel: NoteViewModel

    var POMODORO_WORK = 25  // 25 minut
    var POMODORO_BREAK = 5  // 5 minut

    var v: Int = 8      //4 work_time(%2==0) + 4 break_time(%2==1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPomodoroBinding.inflate(layoutInflater)

        // Set full screen
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(binding.root)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        progressBar = binding.pbTimer
        startBtn = binding.btnPlayPause
        resetBtn = binding.ibReset
        timeLeftTv = binding.tvTimeLeft
        work_break = binding.tvWorkBreakTime


        binding.series1.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.pr1_pomodoro_yellow_on
            ), PorterDuff.Mode.SRC_ATOP
        )
        binding.series4.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.pr1_pomodoro_yellow_off
            ), PorterDuff.Mode.SRC_ATOP
        )
        binding.series3.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.pr1_pomodoro_yellow_off
            ), PorterDuff.Mode.SRC_ATOP
        )
        binding.series2.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.pr1_pomodoro_yellow_off
            ), PorterDuff.Mode.SRC_ATOP
        )

        setTimeFunction(POMODORO_WORK)
        v--
        //Tutaj kod do obslugi trybu nie przeszkadzac
        binding.btnPlayPause.setOnClickListener {
            if(isEnabled)
            {
               disableDoNotDisturbMode()
            }
            else
            {
                enableDoNotDisturbMode()
            }

            if(work_break.equals("Czas na przerwę"))
            {
                turnOff()
            }
        }

        binding.ibReset.setOnClickListener {
            disableDoNotDisturbMode()
            resetTimeEnd()
        }

        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_note_pomodoro, null)
        builder.setView(dialogView)
        val alertDialog = builder.create()

        var note_tmp: String? = ""
        var pomodoroNote: Notes?

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                pomodoroNote = noteViewModel.getNoteById(1)
                if (pomodoroNote?.noteId != 1)
                {
                    noteViewModel.addNote(
                        Notes(
                            noteId = 1,
                            noteTitle = "pomodoro",
                            noteContent = "",
                            photo = null
                        )
                    )
                }
                else
                {
                    note_tmp = pomodoroNote?.noteContent
                }
            }
        }

        binding.ivBackToMenu.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.ivNote.setOnClickListener {

            if (note_tmp != "") {
                dialogView.et_note.setText(note_tmp + '\n')
            }

            dialogView.et_note.setSelection(dialogView.et_note.length())
            dialogView.et_note.requestFocus()

            val window: Window? = alertDialog.getWindow()
            window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

            alertDialog.show()

            dialogView.btn_ok_note.setOnClickListener {
                note_tmp = dialogView.et_note.text.toString()
                noteViewModel.updateNote(Notes(1, "pomodoro", dialogView.et_note.text.toString(), null))
                alertDialog.cancel()
            }

            dialogView.btn_erase_note.setOnClickListener {
                noteViewModel.updateNote(Notes(1, "pomodoro", "", null))
                dialogView.et_note.setText("")
            }
        }

    }
    private fun resetTime() {
        if (timer != null) {
            timer!!.cancel()
            time_progress = 0
            time_selected = POMODORO_WORK
            left_time = 0
            timer = null
            startBtn.text = "Start"
            ready_to_start = true
            progressBar.progress = POMODORO_WORK
            timeLeftTv.text = POMODORO_WORK.toString() + " : 00"
        }
    }

    private fun resetTimeEnd() {
        binding.series4.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.pr1_pomodoro_yellow_off
            ), PorterDuff.Mode.SRC_ATOP
        )
        binding.series4.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.icon_sun_unchecked
            )
        )

        binding.series3?.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.pr1_pomodoro_yellow_off
            ), PorterDuff.Mode.SRC_ATOP
        )
        binding.series3.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.icon_sun_unchecked
            )
        )

        binding.series2.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.pr1_pomodoro_yellow_off
            ), PorterDuff.Mode.SRC_ATOP
        )
        binding.series2?.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.icon_sun_unchecked
            )
        )

        v = 7
        setTimeFunction(POMODORO_WORK)
        resetTime()
    }

    private fun timePause() {
        if (timer != null) {
            timer!!.cancel()
        }
    }

    private fun startTimerSetup() {
        if (time_selected > time_progress) {
            if (ready_to_start) {
                startBtn.text = "Zatrzymaj"
                startTimer(left_time)
                ready_to_start = false
            } else {
                ready_to_start = true
                startBtn.text = "Wznów"
                timePause()
            }
        }

    }

    private fun startTimer(pauseOffSetL: Long) {
        progressBar.progress = time_progress

        timer = object : CountDownTimer(
            (time_selected * 1000).toLong() - pauseOffSetL * 1000, 1000
        ) {
            override fun onTick(p0: Long) {
                time_progress++
                left_time = time_selected.toLong() - p0 / 1000
                progressBar.progress = time_selected - time_progress

                var min: Int = (time_selected - time_progress) / 60
                var sec = (time_selected - time_progress) - min * 60

                timeLeftTv.text = timeFormat(min, sec)
            }

            override fun onFinish() {

                val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        100,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )


                if (v > 0) {
                    if (v % 2 == 1) {
                        work_break.setText("Czas na przerwę")
                        turnOff()

                        if (v == 1) {
                            v--
                            setTimeFunction(30)
                        } else {
                            v--
                            setTimeFunction(POMODORO_BREAK)
                        }

                    } else if (v % 2 == 0) {
                        work_break.setText("Czas na pracę")
                        checkSeries()
                        v--
                        setTimeFunction(POMODORO_WORK)
                    }
                } else {
                    resetTimeEnd()

                }
            }

        }.start()
    }

    fun timeFormat(min: Int, sec: Int): String
    {
        var minTmp: String
        var secTmp: String

        if(min < 10)
            minTmp = "0" + min
        else
            minTmp = min.toString()


        if(sec < 10)
            secTmp = "0" + sec
        else
            secTmp = sec.toString()


        return "$minTmp : $secTmp"
    }

    private fun checkSeries() {
        when (v / 2) {
            1 -> {
                binding.series4.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.pr1_pomodoro_yellow_on
                    ), PorterDuff.Mode.SRC_ATOP
                )
                binding.series4.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icon_sun_checked
                    )
                )
            }
            2 -> {
                binding.series3.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.pr1_pomodoro_yellow_on
                    ), PorterDuff.Mode.SRC_ATOP
                )
                binding.series3.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icon_sun_checked
                    )
                )

            }
            3 -> {
                binding.series2.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.pr1_pomodoro_yellow_on
                    ), PorterDuff.Mode.SRC_ATOP
                )
                binding.series2.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icon_sun_checked
                    )
                )

            }
        }
    }

    private fun setTimeFunction(time: Int) {
        resetTime()
        timeLeftTv.text = "$time : 00"
        time_selected = time*60
        //time_selected = time
        progressBar.max = time_selected
    }

    override fun onDestroy() {
        super.onDestroy()
        if (timer != null) {
            timer?.cancel()
            time_progress = 0
        }
    }

    override fun onBackPressed() {
        // Move the task containing this activity to the back of the activity stack,
        // effectively hiding the activity and allowing the user to return to it later. //// /
        // NavUtils.navigateUpFromSameTask(this)

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

    }

    //Obsluga trybu nocnego(cichego)
    //Wlaczanie
    private fun enableDoNotDisturbMode() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Check if the user has granted access to Do Not Disturb settings
        if (!notificationManager.isNotificationPolicyAccessGranted) {
            // If access has not been granted, redirect the user to the settings page to grant access
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            startActivity(intent)
        } else {
            startTimerSetup()
            // If access has been granted, enable Do Not Disturb mode
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
        }
        isEnabled = true
    }
    //Wylaczanie trybu
    private fun disableDoNotDisturbMode() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (!notificationManager.isNotificationPolicyAccessGranted) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            startActivity(intent)
        } else {
            startTimerSetup()
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
        }
        isEnabled = false
    }

    private fun turnOff()
    {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (!notificationManager.isNotificationPolicyAccessGranted) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            startActivity(intent)
        } else {
            startTimerSetup()
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
        }
    }

}