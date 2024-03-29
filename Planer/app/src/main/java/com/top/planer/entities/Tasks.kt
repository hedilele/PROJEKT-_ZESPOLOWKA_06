package com.top.planer.entities

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
    @ColumnInfo(name = "importance")
    val importance: Int,
    @ColumnInfo(name = "urgency")
    var urgency: Int,
    @ColumnInfo(name = "deadline")
    var deadline: String,
    @ColumnInfo(name = "time_to_finish")
    val timeToFinish: Int,
    @ColumnInfo(name = "is_active")
    var isActive: Int,
    @ColumnInfo(name = "type_id")
    val typeId: Int?,
    @ColumnInfo(name = "note_id")
    var noteId: Int?,
    @ColumnInfo(name = "date")
    val date: String?
)
