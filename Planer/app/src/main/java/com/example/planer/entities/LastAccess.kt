package com.example.planer.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "LastAccess")
data class LastAccess(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int = 1,
    @ColumnInfo(name = "last_access_date")
    var lastAccessDate: LocalDate
)
