package com.example.planer.algorithm

import android.os.Build
import com.example.planer.entities.Tasks
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BlockListTask (
    val list : List<Tasks>
){
    private val tasks = list
    public var today_list : ArrayList<Tasks>? = null
    public var tomorrow_list : ArrayList<Tasks>? = null
    public var week_list : ArrayList<Tasks>? = null
    public var month_list : ArrayList<Tasks>? = null
    public var rest_list : ArrayList<Tasks>? = null


    fun planner(){
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.now().format(formatter)
        val date2 = LocalDate.now()
        println(date2)
    }
}