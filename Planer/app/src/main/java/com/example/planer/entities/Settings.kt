package com.example.planer.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Settings")
data class Settings (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "available_weekday_hours")
    var availableWeekdayHours: Int = 5,
    @ColumnInfo(name = "available_weekend_hours")
    var availableWeekendHours: Int = 6,
    )