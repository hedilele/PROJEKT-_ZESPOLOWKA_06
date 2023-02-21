package com.example.planer.tasks

import java.util.Date

//klasa dziedzicząca z Taska, która rozszerza zadania o parametry służące do tego, by zadanie pojawiało się co określony czas
class Cyclical(
    _number: Int,
    _timescale: Int,
    _title: String,
    _priority: Int,
    _deadline: Date?,
    _time_to_finish: Int,
    _note: String,
    _active: Boolean,
    _type: String,
    _is_calendar: Boolean,
    _photo: String

) : Task(
    _title,
    _priority,
    _deadline,
    _time_to_finish,
    _note,
    _active,
    _type,
    _is_calendar,
    _photo
){
    var number: Int = _number
    var timescale: Int = _timescale
}

