package com.example.planer.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Calendar")
data class Calendar (
            @PrimaryKey(autoGenerate = true)
            @ColumnInfo(name = "id")
            val id: Long, //TODO poprawic Inty na Longi - w kazdej Entity
            @ColumnInfo("start_date")
            val startDate: String,
            @ColumnInfo("end_date")
            val endDate: String,
            @ColumnInfo("type_id")
            val typeId: Int,
            @ColumnInfo("reminder")
            val reminder: Int,
            @ColumnInfo("location")
            val location: String,
            @ColumnInfo("repeat_id")
            val repeatId: Int,
            @ColumnInfo("note_id")
            val noteId: Int,
            @ColumnInfo("name")
            val name: String
)
