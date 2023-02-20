package com.example.planer.meat

import java.util.Date

//obiekt tej klasy ma za zadanie przechowywać taski ukończone
class Finished {
    var finished: ArrayList<Field>? = null

    class Field{
        var task: Task? = null
        var date: Date? = null
    }
}