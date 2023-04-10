package com.example.planer

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.ViewModel.ScopeViewModel
import com.example.planer.databinding.ActivityScopeModeBinding
import com.example.planer.entities.Tasks
import com.example.planer.scope.CardAdapter
import kotlinx.coroutines.coroutineScope


class ScopeMode : AppCompatActivity(), CardAdapter.OnButtonClickListener {

    private lateinit var adapter: CardAdapter
    private val viewModel: ScopeViewModel by viewModels()
    private lateinit var binding: ActivityScopeModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScopeModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CardAdapter(emptyMap())
        adapter.setOnItemClickListener(this)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                val firstItem = binding.recyclerView.findViewHolderForAdapterPosition(0)?.itemView
                val padding = firstItem?.height ?: 0
                binding.recyclerView.setPadding(0, padding, 0, 0)
            }
        })

        viewModel.getOverdueTasks().observe(this) { tasks ->
            adapter.updateList(tasks)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun onLaterButtonClick(item: Tasks) {
        coroutineScope {
            viewModel.postponeTaskTommorow(item)
        }
    }

    override suspend fun onDoneButtonClick(item: Tasks) {
        coroutineScope {
            viewModel.setTaskAsDone(item)
        }
    }
}
