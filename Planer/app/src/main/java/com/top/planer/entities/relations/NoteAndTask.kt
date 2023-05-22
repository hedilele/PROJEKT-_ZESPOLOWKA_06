package com.top.planer.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.top.planer.entities.Notes
import com.top.planer.entities.Tasks

data class NoteAndTask(
    @Embedded
    val notes: Notes,
    @Relation(
        parentColumn = "id",
        entityColumn = "note_id"
    )
    val tasks: Tasks
)
