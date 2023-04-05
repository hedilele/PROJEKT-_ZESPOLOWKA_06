package com.example.planer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.ViewModel.ScopeViewModel
import com.example.planer.entities.relations.NoteAndTask
import com.example.planer.scope.CardAdapter


class ScopeMode : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var scopeViewModel: ScopeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scope_mode)

        scopeViewModel = ViewModelProvider(this)[ScopeViewModel::class.java]


        recyclerView = findViewById(R.id.recycler_view)
        val adapter = CardAdapter(scopeViewModel.overdueTasks)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

}
