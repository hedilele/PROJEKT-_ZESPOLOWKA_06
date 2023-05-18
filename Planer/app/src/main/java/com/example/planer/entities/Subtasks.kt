package com.example.planer.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subtasks")
data class Subtasks(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo("task_id")
    val taskId: Int,
    @ColumnInfo("is_active")
    val isActive: Boolean
)
