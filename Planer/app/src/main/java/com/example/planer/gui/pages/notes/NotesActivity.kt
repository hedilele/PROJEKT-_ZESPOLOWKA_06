package com.example.planer.gui.pages.home.notes

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planer.R
import com.example.planer.ViewModel.HabitViewModel
import com.example.planer.ViewModel.NoteViewModel
import com.example.planer.databinding.ActivityNotesBinding
import com.example.planer.databinding.FragmentHomeBinding
import com.example.planer.entities.Habits
import com.example.planer.entities.Notes
import com.example.planer.gui.pages.home.habits.AdapterHabits
import kotlinx.android.synthetic.main.dialod_when_title_empty.view.*
import kotlinx.android.synthetic.main.dialog_habit.view.*
import kotlinx.android.synthetic.main.dialog_note_pomodoro.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.single_short_note.view.*
import kotlinx.coroutines.launch
import java.net.URLEncoder

class NotesActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var binding: ActivityNotesBinding

    var list = mutableListOf<Notes>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)

        // Set full screen
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(binding.root)


        val rvh = binding.shortNoteList
        val adapter = AdapterNotes(
            list,
            {noteDelete -> lifecycleScope.launch { noteViewModel.deleteNote(noteDelete) } },
            {noteUpdate -> lifecycleScope.launch { noteViewModel.updateNote(noteUpdate) } },
            {note ->
                // Encode the selected item title to be used in the search URL
                val encodedQuery = URLEncoder.encode(note, "UTF-8")

                // Create an intent with the Google search URL
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://www.google.com/search?q=$encodedQuery")
                }

                // Start the activity to redirect the user to the Google search page
                startActivity(intent)
            }
        )

        rvh.adapter = adapter
        rvh.layoutManager = LinearLayoutManager(applicationContext)

        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        //habitViewModel = ViewModelProvider(this)[HabitViewModel::class.java]
        noteViewModel.readAllShortNotes.observe(this, Observer {
            adapter.updateList(it.toMutableList())
        })




        binding.btnAddNote.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            val inflater = LayoutInflater.from(this)
            val dialogView = inflater.inflate(R.layout.dialog_short_note, null)
            builder.setView(dialogView) //Podlaczanie xmla

            val content = dialogView.findViewById<AppCompatEditText>(R.id.note_content)
            val btn_edit = dialogView.findViewById<Button>(R.id.btn_edit)
            btn_edit.setText("Stwórz")

            val alertDialog = builder.create()
            alertDialog.show()

            content.setText("")

            content.requestFocus()
            val window: Window? = alertDialog.getWindow()
            window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

            btn_edit.setOnClickListener {

                if(content.text.toString().replace(" ", "") == "")
                {
                    val builder = androidx.appcompat.app.AlertDialog.Builder(applicationContext)
                    val inflater = LayoutInflater.from(applicationContext)
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

//                val parentView = dialogView.parent as ViewGroup
//                parentView?.removeView(dialogView)
            }



        }

        binding.ivBackToMenu.setOnClickListener {
            finish()
        }

    }
}