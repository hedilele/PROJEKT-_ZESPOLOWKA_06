package com.example.planer.gui.pages

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.example.planer.R
import com.example.planer.databinding.FragmentHomeBinding
import com.example.planer.databinding.FragmentPomodoroBinding

class PomodoroFragment : Fragment() {

    private lateinit var binding: FragmentPomodoroBinding

    private var timeSelected : Int = 6
    private var timeCountDown: CountDownTimer? = null
    private var timeProgress = 0
    private var pauseOffSet: Long = 0
    private var isStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pomodoro, container, false)
        binding = FragmentPomodoroBinding.inflate(layoutInflater)


        binding.btnAdd.setOnClickListener {
           // setTimeFunction()
        }
        binding.btnPlayPause.setOnClickListener {
            startTimerSetup()
        }

        binding.ibReset.setOnClickListener {
            resetTime()
        }

        binding.tvAddTime.setOnClickListener {
            addExtraTime()
        }



        return view

    }

    private fun addExtraTime()
    {
        if (timeSelected!=0)
        {
            timeSelected+=15
            binding.pbTimer.max = timeSelected
            timePause()
            startTimer(1)
            Toast.makeText(context,"15 sec added",Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetTime()
    {
        if (binding.pbTimer!=null)
        {
            timeCountDown!!.cancel()
            timeProgress=0
            timeSelected=0
            pauseOffSet=0
            timeCountDown=null
            binding.btnPlayPause.text ="start"
            isStart = true
            binding.pbTimer.progress = 0
            binding.tvTimeLeft.text = "0"
        }
    }

    private fun timePause()
    {
        if (timeCountDown!=null)
        {
            timeCountDown!!.cancel()
        }
    }

    private fun startTimerSetup()
    {
        if (timeSelected>timeProgress)
        {
            if (isStart)
            {
                binding.btnPlayPause.text = "Pause"
                startTimer(1)
                isStart = false
            }
            else
            {
                isStart =true
                binding.btnPlayPause.text = "Resume"
                timePause()
            }
        }
        else
        {
            Toast.makeText(context,"Enter Time",Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTimer(pauseOffSetL: Long)
    {
        binding.pbTimer.progress = timeProgress
        timeCountDown = object :CountDownTimer(
            (timeSelected*1000).toLong() - pauseOffSetL*1000, 1000)
        {
            override fun onTick(p0: Long) {
                timeProgress++
                pauseOffSet = timeSelected.toLong()- p0/1000
                binding.pbTimer.progress = timeSelected-timeProgress
                binding.tvTimeLeft.text = (timeSelected - timeProgress).toString()
            }

            override fun onFinish() {
                resetTime()
                Toast.makeText(context,"Times Up!", Toast.LENGTH_SHORT).show()
            }

        }.start()
    }


//    private fun setTimeFunction()
//    {
//        val timeDialog = Dialog(this)
//        timeDialog.setContentView(R.layout.add_dialog)
//        val timeSet = timeDialog.findViewById<EditText>(R.id.etGetTime)
//        val timeLeftTv: TextView = findViewById(R.id.tvTimeLeft)
//        val btnStart: Button = findViewById(R.id.btnPlayPause)
//        val progressBar = findViewById<ProgressBar>(R.id.pbTimer)
//        timeDialog.findViewById<Button>(R.id.btnOk).setOnClickListener {
//            if (timeSet.text.isEmpty())
//            {
//                Toast.makeText(this,"Enter Time Duration",Toast.LENGTH_SHORT).show()
//            }
//            else
//            {
//                resetTime()
//                timeLeftTv.text = timeSet.text
//                btnStart.text = "Start"
//                timeSelected = timeSet.text.toString().toInt()
//                progressBar.max = timeSelected
//            }
//            timeDialog.dismiss()
//        }
//        timeDialog.show()
//    }

    override fun onDestroy() {
        super.onDestroy()
        if(timeCountDown!=null)
        {
            timeCountDown?.cancel()
            timeProgress=0
        }
    }

}


