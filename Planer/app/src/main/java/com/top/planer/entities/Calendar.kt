package com.top.planer.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Calendar")
data class Calendar (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long=0,
    @ColumnInfo("start_date")
    val startDate: String="",
    @ColumnInfo("end_date") // użytkownik może zaznaczyć i początek i koniec, domyślnie wydarzenie trwa 1h
    val endDate: String="",
    @ColumnInfo("type_id")// tu przechowuje id parent eventu, moze sie przyda potem
    val typeId: Int=0,
    @ColumnInfo("reminder")
    val reminder: Int=0,
    @ColumnInfo("location") // można dodać, opcjonalnie
    val location: String="",
    @ColumnInfo("repeat_id") // co 0- wcale 1 - tydzień, miesiąc, rok , (np. na 5 lat wprzód)
    val repeatId: Int=0,
    @ColumnInfo("note_id")
    var noteId: Int,
    @ColumnInfo("name")
    val name: String=""
)
