package com.example.planer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.ViewModel.ScopeViewModel
import com.example.planer.entities.relations.NoteAndTask
import com.example.planer.scope.CardAdapter


class ScopeMode : AppCompatActivity() {

    private lateinit var adapter: CardAdapter
    private lateinit var dataSet: LiveData<List<NoteAndTask>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scope_mode)

        adapter = CardAdapter(emptyList())

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.post {
            val firstItem = recyclerView.findViewHolderForAdapterPosition(0)?.itemView
            val padding = firstItem?.height ?: 0
            recyclerView.setPadding(0, padding, 0, 0)
        }

        val viewModel = ViewModelProvider(this)[ScopeViewModel::class.java]
        dataSet = viewModel.getOverdueTasks()

        dataSet.observe(this) { newTasks ->
            adapter.updateList(newTasks)
        }
    }
}
