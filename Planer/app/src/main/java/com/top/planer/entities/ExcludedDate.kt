package com.top.planer.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "ExcludedDate", indices = [Index(value = ["date"], unique = true)])
data class ExcludedDate (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "date") // Proszę formatować jako datę YYYY-MM-DD (ISO 8601)
    val excludedDate: LocalDate
    )
