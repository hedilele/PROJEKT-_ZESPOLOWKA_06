package com.example.planer.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.planer.entities.Calendar
import com.example.planer.entities.Repeat

data class RepeatAndCalendar(
    @Embedded
    val repeat: Repeat,
    @Relation(
        parentColumn = "id",
        entityColumn = "repeat_id"
    )
    val calendar: Calendar
)
