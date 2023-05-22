package com.top.planer.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.top.planer.entities.Tasks
import com.top.planer.entities.Types


data class TypeAndTasks (
        @Embedded
        val types: Types,
        @Relation(
                parentColumn = "id",
                entityColumn = "id"
        )
        val tasks: List<Tasks>
)
