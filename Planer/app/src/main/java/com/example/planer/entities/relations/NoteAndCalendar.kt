package com.example.planer.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.planer.entities.Calendar
import com.example.planer.entities.Notes
import com.example.planer.entities.Repeat

data class NoteAndCalendar(
    @Embedded
    val notes: Notes,
    @Relation(
        parentColumn = "id",
        entityColumn = "note_id"
    )
    val calendar: Calendar
)