package com.top.planer.gui.pages.guide

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.top.planer.R
import kotlinx.android.synthetic.main.fragment_guide_home_page.view.*
class GuideHomePageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_guide_home_page, container, false)


        var tmp = "<font color='#4E3834'>WAŻNE</font>"
        view.important.setText(Html.fromHtml(tmp + view.important.text))

        tmp = "<font color='#4E3834'>PILNE</font>"
        view.urgent.setText(Html.fromHtml(tmp + view.urgent.text))


        return view
    }
}