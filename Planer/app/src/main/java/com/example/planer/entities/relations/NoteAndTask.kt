package com.example.planer.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.planer.entities.Notes
import com.example.planer.entities.Tasks

data class NoteAndTask(
    @Embedded
    val notes: Notes,
    @Relation(
        parentColumn = "id",
        entityColumn = "note_id"
    )
    val tasks: Tasks
)
