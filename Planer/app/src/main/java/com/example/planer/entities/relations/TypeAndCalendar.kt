package com.example.planer.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.planer.entities.Calendar
import com.example.planer.entities.Finished
import com.example.planer.entities.Tasks
import java.sql.Types

//1:n
data class TypeAndCalendar(
    @Embedded
    val types: Types,
    @Relation(
        parentColumn = "id",
        entityColumn = "type_id" //albo "type_id - SPRAWDZIC todo
    )
    val calendar: List<Calendar>
)
