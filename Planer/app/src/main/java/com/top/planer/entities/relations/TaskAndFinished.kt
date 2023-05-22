package com.top.planer.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.top.planer.entities.Finished
import com.top.planer.entities.Tasks

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
