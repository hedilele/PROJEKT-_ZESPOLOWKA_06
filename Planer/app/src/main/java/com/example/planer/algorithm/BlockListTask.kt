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




        //rozdzielenie tasków do odpowiednich przedziałów
        for (i in tasks){
            val d = EasyDate(i.deadline)
            when{

                //zadanie, które miało termin do dzisiaj, a się nie mieści, zostaje przesunięte na jutro, lub jeśli się nie da - do końca tyg
                d.date == today.date -> {

                    //deadline <=3 -> task staje się pilny
                    if(i.importance == 1)
                        i.urgency = 1

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

                //zadanie, które miało termin do jutra, a się nie mieści, zostaje przesunięte na do końca tyg
                d.date == (today+1).date -> {

                    //deadline <=3 -> task staje się pilny
                    if(i.importance == 1)
                        i.urgency = 1

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

        //sortowanie przedziałów najpierw po ważności, potem pilności i na koniec po czasie trwania
        today_list.sortedWith(compareBy({it.importance}, {it.urgency}, {it.timeToFinish}))
        tomorrow_list.sortedWith(compareBy({it.importance}, {it.urgency}, {it.timeToFinish}))
        week_list.sortedWith(compareBy({it.importance}, {it.urgency}, {it.timeToFinish}))
        month_list.sortedWith(compareBy({it.importance}, {it.urgency}, {it.timeToFinish}))
        rest_list.sortedWith(compareBy({it.importance}, {it.urgency}, {it.timeToFinish}))
    }

}