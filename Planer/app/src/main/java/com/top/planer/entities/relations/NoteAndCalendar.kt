package com.top.planer.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.top.planer.entities.Calendar
import com.top.planer.entities.Notes

data class NoteAndCalendar(
    @Embedded
    val notes: Notes,
    @Relation(
        parentColumn = "id",
        entityColumn = "note_id"
    )
    val calendar: Calendar
)