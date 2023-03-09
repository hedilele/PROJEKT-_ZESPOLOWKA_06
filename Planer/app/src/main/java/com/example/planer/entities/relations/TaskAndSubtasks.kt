package com.example.planer.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.planer.entities.Subtasks
import com.example.planer.entities.Tasks
import com.example.planer.entities.Types

data class TaskAndSubtasks(
    @Embedded
    val tasks: Tasks,
    @Relation(
        parentColumn = "id",
        entityColumn = "task_id" //albo "type_id - SPRAWDZIC todo
    )
    val subtasks: List<Subtasks>
)