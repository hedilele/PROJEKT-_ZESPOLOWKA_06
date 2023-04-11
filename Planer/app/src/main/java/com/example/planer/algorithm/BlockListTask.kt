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

    var todayWork = 0
    var tomorrowWork = 0


    /**
     * Funkcja układająca plan użytkownikowi
     */
    fun planner(){
        val today = EasyDate(LocalDate.now())
        for (i in tasks){
            val d = EasyDate(i.deadline)
            when{

                d.date == today.date -> {
                    when{
                        todayWork + i.timeToFinish <= 60 -> {
                            today_list.add(i)
                            todayWork += i.timeToFinish
                        }
                        tomorrowWork + i.timeToFinish <= 60 -> {
                            tomorrow_list.add(i)
                            tomorrowWork += i.timeToFinish
                        }
                        else -> week_list.add(i)
                    }
                }

                d.date == (today+1).date -> {
                    if(tomorrowWork + i.timeToFinish <= 60 ){
                        tomorrow_list.add(i)
                        tomorrowWork += i.timeToFinish
                    }
                    else {
                        week_list.add(i)
                    }
                }

                d.date <= (today+7).date -> week_list.add(i)

                d.date <= (today+31).date -> month_list.add(i)

                else -> rest_list.add(i)
            }
        }
        today_list.sortedWith(compareBy({it.importance}, {it.urgency}, {it.timeToFinish}))
        tomorrow_list.sortedWith(compareBy({it.importance}, {it.urgency}, {it.timeToFinish}))
        week_list.sortedWith(compareBy({it.importance}, {it.urgency}, {it.timeToFinish}))
        month_list.sortedWith(compareBy({it.importance}, {it.urgency}, {it.timeToFinish}))
        rest_list.sortedWith(compareBy({it.importance}, {it.urgency}, {it.timeToFinish}))
    }

}