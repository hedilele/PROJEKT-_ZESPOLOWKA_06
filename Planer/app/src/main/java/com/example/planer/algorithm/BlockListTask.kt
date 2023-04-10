package com.example.planer.algorithm

import com.example.planer.entities.Tasks
import java.time.LocalDate

/**
 * Klasa, która rozdziela listę tasków na poszczególne przedziały czasowe.
 */
class BlockListTask (
    var list : List<Tasks>
){
    private val tasks = list
    var today_list = mutableListOf<Tasks>()
    var tomorrow_list = mutableListOf<Tasks>()
    var week_list = mutableListOf<Tasks>()
    var month_list = mutableListOf<Tasks>()
    var rest_list = mutableListOf<Tasks>()


    /**
     * Funkcja układająca plan użytkownikowi
     */
    fun planner(){
        val today = EasyDate(LocalDate.now())
        var log  = ""
        for (i in tasks){
            val d = EasyDate(i.deadline)
            if(d.date == today.date){
                log += "${today.date} "
                today_list.add(i)
            }
            else if(d.date == (today+1).date){
                log += "${(today+1).date} "
                tomorrow_list.add(i)
            }
            else if(d.date <= (today+7).date){
                log += "${(today+7).date} "
                week_list.add(i)
            }
            else if(d.date <= (today+31).date){
                log += "${(today+31).date} "
                month_list.add(i)
            }
            else{
                log += "${today.date} "
                rest_list.add(i)
            }

            log += "  |   ${d.date}\n"
        }
        throw Exception(log)
    }
}