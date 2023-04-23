package com.example.planer.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.planer.entities.Tasks
import com.example.planer.entities.Types


data class TypeAndTasks (
        @Embedded
        val types: Types,
        @Relation(
                parentColumn = "id",
                entityColumn = "id"
        )
        val tasks: List<Tasks>
)
