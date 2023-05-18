package com.example.planer.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
//Zaprojektowac DAO do tego, dodatkowo relacje i polaczyc to - dalsze doszkalanie z dokumentacji
//i filmikow
//Dok https://developer.android.com/training/data-storage/room/defining-data#kotlin

@Entity(tableName = "Notes")
data class Notes(
    //Mozliwe jakies nulle ? - doda sie po uzgodnieniu
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val noteId: Int = 0,
    @ColumnInfo(name = "note_title")
    val noteTitle: String,
    @ColumnInfo(name = "note_content")
    var noteContent: String,
    @ColumnInfo(name = "photo")
    val photo: String?

)