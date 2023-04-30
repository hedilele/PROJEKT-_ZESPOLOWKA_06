package com.example.planer.gui.pages.settings

import com.example.planer.entities.Calendar
import com.example.planer.entities.ExcludedDate
import com.example.planer.entities.Habits
import com.example.planer.entities.Notes
import com.example.planer.entities.Settings
import com.example.planer.entities.Tasks
import com.example.planer.entities.Types

data class DbExport(
    val calendar: List<Calendar>,
    val excludedDate: List<ExcludedDate>,
    val habits: List<Habits>,
    val notes: List<Notes>,
    val settings: Settings,
    val tasks: List<Tasks>,
    val types: List<Types>
)
