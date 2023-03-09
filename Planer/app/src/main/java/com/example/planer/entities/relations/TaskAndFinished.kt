package com.example.planer.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.planer.entities.Finished
import com.example.planer.entities.Tasks

//1:n
data class TaskAndFinished(
    @Embedded
    val tasks: Tasks,
    @Relation(
        parentColumn = "id",
        entityColumn = "task_id" //albo "type_id - SPRAWDZIC todo
    )
    val finished: List<Finished>
)
