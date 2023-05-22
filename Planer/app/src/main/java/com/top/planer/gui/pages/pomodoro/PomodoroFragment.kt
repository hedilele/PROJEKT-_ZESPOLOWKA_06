package com.top.planer.gui.pages.pomodoro

import android.content.Context
import android.graphics.PorterDuff
import android.os.*
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.top.planer.R
import com.top.planer.ViewModel.NoteViewModel
import kotlinx.android.synthetic.main.fragment_pomodoro.view.*

class PomodoroFragment : Fragment() {

    private var time_selected: Int = 0
    private var timer: CountDownTimer? = null
    private var time_progress = 0
    private var left_time: Long = 0
    private var ready_to_start = true

    lateinit var progressBar: ProgressBar
    lateinit var startBtn: Button
    lateinit var resetBtn: Button
    lateinit var timeLeftTv: TextView
    lateinit var work_break: TextView

    private lateinit var noteViewModel: NoteViewModel

    var POMODORO_WORK = 4
    var POMODORO_BREAK = 3

    var v: Int = 8      //4 work_time(%2==0) + 4 break_time(%2==1)
    var actual_time = 4

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return view
    }

    private fun resetTime() {
        if (timer != null) {
            timer!!.cancel()
            time_progress = 0
            time_selected = POMODORO_WORK
            left_time = 0
            timer = null
            startBtn.text = "Staert"
            ready_to_start = true
            progressBar.progress = POMODORO_WORK
            timeLeftTv.text = POMODORO_WORK.toString() + " : 00"

        }
    }

    private fun resetTimeEnd() {
        view?.series_4?.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.pr1_pomodoro_yellow_off
            ), PorterDuff.Mode.SRC_ATOP
        )
        view?.series_4?.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.icon_sun_unchecked
            )
        )

        view?.series_3?.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.pr1_pomodoro_yellow_off
            ), PorterDuff.Mode.SRC_ATOP
        )
        view?.series_3?.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.icon_sun_unchecked
            )
        )

        view?.series_2?.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.pr1_pomodoro_yellow_off
            ), PorterDuff.Mode.SRC_ATOP
        )
        view?.series_2?.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.icon_sun_unchecked
            )
        )

        v = 7
        setTimeFunction(5)
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
                startBtn.text = "Pause"
                startTimer(left_time)
                ready_to_start = false
            } else {
                ready_to_start = true
                startBtn.text = "Resume"
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

                timeLeftTv.text = "$min : $sec"
            }

            override fun onFinish() {

                val vibrator =
                    requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            100,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    // Deprecated in API 26
                    vibrator.vibrate(100)
                }


                if (v > 0) {
                    if (v % 2 == 1) {
                        work_break.setText("Czas na przerwę")

                        if (v == 1) {
                            v--
                            setTimeFunction(20)
                        } else {
                            v--
                            setTimeFunction(3)
                            actual_time = POMODORO_BREAK
                        }

                    } else if (v % 2 == 0) {
                        work_break.setText("Czas na pracę")
                        checkSeries(view)
                        v--
                        setTimeFunction(4)
                        actual_time = POMODORO_WORK
                    }
                } else {
                    resetTimeEnd()

                }
            }

        }.start()
    }

    private fun checkSeries(view: View?) {
        when (v / 2) {
            1 -> {
                view?.series_4?.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.pr1_pomodoro_yellow_on
                    ), PorterDuff.Mode.SRC_ATOP
                )
                view?.series_4?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.icon_sun_checked
                    )
                )
            }
            2 -> {
                view?.series_3?.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.pr1_pomodoro_yellow_on
                    ), PorterDuff.Mode.SRC_ATOP
                )
                view?.series_3?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.icon_sun_checked
                    )
                )

            }
            3 -> {
                view?.series_2?.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.pr1_pomodoro_yellow_on
                    ), PorterDuff.Mode.SRC_ATOP
                )
                view?.series_2?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.icon_sun_checked
                    )
                )

            }
        }
    }

    private fun setTimeFunction(time: Int) {
        resetTime()
        timeLeftTv.text = "$time : 00"
        //timeSelected = time*60
        time_selected = time
        progressBar.max = time_selected
    }

    override fun onDestroy() {
        super.onDestroy()
        if (timer != null) {
            timer?.cancel()
            time_progress = 0
        }
    }
}
