package com.example.planer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.scope.CardAdapter


class ScopeMode : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scope_mode)

        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        recyclerView = findViewById(R.id.recycler_view)
        val adapter = CardAdapter(taskViewModel.readAllData)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

}
