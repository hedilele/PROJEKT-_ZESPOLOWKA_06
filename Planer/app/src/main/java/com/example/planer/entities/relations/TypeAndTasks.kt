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
                entityColumn = "type_id" //albo "type_id - SPRAWDZIC todo
        )
        val tasks: List<Tasks>
)
