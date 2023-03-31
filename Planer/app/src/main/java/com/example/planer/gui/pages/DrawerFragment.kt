package com.example.planer.gui.pages

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.planer.R
import com.example.planer.databinding.FragmentDrawerBinding
import kotlinx.android.synthetic.main.activity_main.view.*


class DrawerFragment : Fragment() {

    private lateinit var binding: FragmentDrawerBinding


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_drawer, container, false)
        binding = FragmentDrawerBinding.inflate(layoutInflater)


        return view
    }




}