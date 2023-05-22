package com.top.planer.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.top.planer.entities.Calendar
import com.top.planer.entities.Repeat

data class RepeatAndCalendar(
    @Embedded
    val repeat: Repeat,
    @Relation(
        parentColumn = "id",
        entityColumn = "repeat_id"
    )
    val calendar: Calendar
)
