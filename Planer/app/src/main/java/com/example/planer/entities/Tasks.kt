package com.example.planer.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Tasks(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int=0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo("priority")
    val priority: Int,
    @ColumnInfo(name = "deadline")
    val deadline: String,
    @ColumnInfo(name = "time_to_finish")
    val timeToFinish: Int,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean,
    @ColumnInfo(name = "type_id")
    val typeId: Int,
    @ColumnInfo(name = "is_calendar")
    val isCalendar: Boolean,
    @ColumnInfo(name = "habit_id")
    val habitId: Int
)
