package com.example.planer.gui.pages.home.tasks

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.planer.R

class EditTaskDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let{
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(requireActivity().layoutInflater.inflate(R.layout.activity_adding_task, null))

            alertDialog.create()
        }?:throw IllegalStateException("")
    }
}