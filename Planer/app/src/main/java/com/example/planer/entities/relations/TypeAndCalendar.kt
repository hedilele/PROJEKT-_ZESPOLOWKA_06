package com.example.planer.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.planer.entities.Calendar
import com.example.planer.entities.Types

//1:n
data class TypeAndCalendar(
    @Embedded
    val types: Types,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val calendar: List<Calendar>
)
