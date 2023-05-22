package com.top.planer.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.top.planer.entities.Subtasks
import com.top.planer.entities.Tasks

data class TaskAndSubtasks(
    @Embedded
    val tasks: Tasks,
    @Relation(
        parentColumn = "id",
        entityColumn = "task_id" //albo "type_id - SPRAWDZIC todo
    )
    val subtasks: List<Subtasks>
)