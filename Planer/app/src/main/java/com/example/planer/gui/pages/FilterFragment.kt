package com.example.planer.gui.pages

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.planer.R
import kotlinx.android.synthetic.main.fragment_filter.view.*

class FilterFragment : Fragment(){

    //Podlaczanie xmla dialog_filter
    //private lateinit var binding: FragmentFilterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_filter, container, false)
        //Po kliknieciu na lejek ImageView
        //binding = FragmentFilterBinding.inflate(layoutInflater)
        view.searchView.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            //builder.setView(R.layout.activity_adding_task)

            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.dialog_filter, null)

            builder.setView(dialogView) //Podlaczanie xmla

            val alertDialog = builder.create()
            alertDialog.show()
            //TODO - przyciski i cala obsluga okna
        }
        return view
    }

}