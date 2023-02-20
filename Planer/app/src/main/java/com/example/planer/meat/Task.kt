package com.example.planer.meat

import java.util.Date

//klasa task dziedziczy z subtask
// to co się dzieje tu na dole to tylko konstruktor, a {} wypisane atrybuty

open class Task(

    _title: String,
    _priority: Int,
    _deadline: Date?,
    _time_to_finish: Int,
    _note: String,
    _active: Boolean,
    _type: String,
    _is_calendar: Boolean,
    _photo: String,

    ) : Subtask(_title, _active){

    var priority: Int = _priority
    var deadline: Date? = _deadline
    var time_to_finish: Int = _time_to_finish
    var note: String = _note
    var type: String = _type
    var is_calendar: Boolean = _is_calendar
    var photo: String = _photo

    // uwaga! atrybut subtasks przy tworzeniu nowego obiektu tej klasy przyjmuje wartość null, gdyż subtaski będą dodawane w późniejszym etapie
    var subtasks: ArrayList<Subtask>? = null

}

