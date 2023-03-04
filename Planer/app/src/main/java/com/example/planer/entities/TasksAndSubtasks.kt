package com.example.planer.entities

import androidx.room.Embedded
import androidx.room.Relation

data class TasksAndSubtasks(
    @Embedded val tasks: Tasks,
    @Relation(
        parentColumn = "id",
        entityColumn = "task_id"
    )
    val subtasks: Subtasks
)
