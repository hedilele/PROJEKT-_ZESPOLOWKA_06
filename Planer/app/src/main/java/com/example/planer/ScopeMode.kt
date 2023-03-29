package com.example.planer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.entities.Tasks
import com.example.planer.scope.CardAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext


class ScopeMode : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scope_mode)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        recyclerView = findViewById(R.id.recycler_view)
        val adapter = CardAdapter(userViewModel.readAllData)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

}
