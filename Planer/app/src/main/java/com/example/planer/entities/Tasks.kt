package com.example.planer.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

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
    val urgency: Int,
    @ColumnInfo(name = "deadline")
    val deadline: String,
    @ColumnInfo(name = "time_to_finish")
    val timeToFinish: Int,
    @ColumnInfo(name = "is_active")
    val isActive: Int,
    @ColumnInfo(name = "type_id")
    val typeId: Int,
    @ColumnInfo(name = "note_id")
    val noteId: Int,
    //@ColumnInfo(name = "date")
    //val date: String

)
