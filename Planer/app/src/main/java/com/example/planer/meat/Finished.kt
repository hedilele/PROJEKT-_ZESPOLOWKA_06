package com.example.planer.meat

import java.util.Date

class Finished {
    var finished: ArrayList<Field>? = null

    class Field{
        var task: Task? = null
        var date: Date? = null
    }
}