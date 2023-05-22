package com.example.planer.gui.pages.scope

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.ViewModel.ScopeViewModel
import com.example.planer.databinding.ActivityScopeModeBinding
import com.example.planer.entities.Notes
import com.example.planer.entities.Tasks
import kotlinx.coroutines.coroutineScope
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ScopeMode : AppCompatActivity(), CardAdapter.OnButtonClickListener {

    private lateinit var adapter: CardAdapter
    private val viewModel: ScopeViewModel by viewModels()
    private lateinit var binding: ActivityScopeModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        // Set full screen
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)

        viewModel.getTypes().observe(this) {
            adapter.updateTypes(it)
        }

        binding = ActivityScopeModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CardAdapter(emptyMap(), emptyList())
        adapter.setOnItemClickListener(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = UnscrollableLinearLayoutManager(this)

        adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                val firstItem = binding.recyclerView.findViewHolderForAdapterPosition(0)?.itemView
                val padding = firstItem?.height ?: 0
                binding.recyclerView.setPadding(0, padding, 0, 0)
            }
        })

        viewModel.getScopeTasks().observe(this) { tasks ->
            if (tasks.isEmpty()) {
                binding.notasksleft.visibility = VISIBLE
            } else {
                binding.notasksleft.visibility = INVISIBLE
            }
            adapter.updateList(tasks)
        }

        binding.ivBackToMenu.setOnClickListener {
            finish()
        }
    }

    override suspend fun onLaterButtonClick(item: Tasks) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        if (item.importance == 0 && item.urgency == 0
            && LocalDateTime.now().isBefore(LocalDateTime.parse(item.deadline, formatter))) {
            viewModel.removeTask(item)
        } else {
            coroutineScope {
                viewModel.postponeTaskTomorrow(item)
            }
        }
    }

    override suspend fun onDoneButtonClick(task: Tasks, note: Notes?) {
        coroutineScope {
            viewModel.setTaskAsDone(this@ScopeMode, task, note)
        }
    }
}
