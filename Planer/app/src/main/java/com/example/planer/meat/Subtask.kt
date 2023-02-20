package com.example.planer.meat

//klasa podzadań, które można dodawać do tasków
open class Subtask(_title: String, _active: Boolean){
    var title: String = _title
    var active: Boolean = _active
}