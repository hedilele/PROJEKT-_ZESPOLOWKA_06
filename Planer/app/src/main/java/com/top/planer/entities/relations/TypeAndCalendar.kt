package com.top.planer.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.top.planer.entities.Calendar
import com.top.planer.entities.Types

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
