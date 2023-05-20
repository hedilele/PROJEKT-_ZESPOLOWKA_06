package com.example.planer.gui.pages.guide

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.planer.R
import kotlinx.android.synthetic.main.fragment_guide_pomodoro.view.*


class GuidePomodoroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_guide_pomodoro, container, false)

        val textView: TextView = view.textViewNote

        var ssb: SpannableStringBuilder = SpannableStringBuilder()
        ssb.append(textView.text.toString())
        ssb.setSpan(ImageSpan(requireContext(), R.drawable.icon_note_pomodoro), ssb.length-1, ssb.length, 0)
        textView.setText(ssb)

        return view
    }


}