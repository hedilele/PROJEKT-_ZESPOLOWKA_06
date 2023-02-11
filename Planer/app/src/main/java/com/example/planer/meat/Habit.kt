package com.example.planer.meat

import java.util.Date


//v.1
/*
class Habit {
    var habits: ArrayList<Field>? = null

    class Field{
        var task: Task? = null
        var number: Int = 0
        var timescale: Int = 0
    }

    //timescale: enum
    //number x timescale
}*/

//v.2
class Habit(
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

