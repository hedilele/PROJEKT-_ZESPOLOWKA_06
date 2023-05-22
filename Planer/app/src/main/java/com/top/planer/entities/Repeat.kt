package com.top.planer.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Repeat")
data class Repeat (
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val id: Int,
        @ColumnInfo(name = "number")
        val number: Int,
        @ColumnInfo(name = "timescale")
        val timescale: Int
)