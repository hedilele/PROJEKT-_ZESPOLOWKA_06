package com.example.planer.algorithm

import com.example.planer.entities.Tasks
import java.time.LocalDate
import java.util.Queue

/**
 * Klasa, która rozdziela listę tasków na poszczególne przedziały czasowe.
 */
class BlockListTask (
    var list : List<Tasks>
){
    private val tasks = list
    var todayList = mutableListOf<Tasks>()
    var tomorrowList = mutableListOf<Tasks>()
    var weekList = mutableListOf<Tasks>()
    var monthList = mutableListOf<Tasks>()
    var restList = mutableListOf<Tasks>()


    /**
     * Funkcja układająca plan użytkownikowi
     */
    fun planner(){

        var tomorrowFakeList = mutableListOf<Tasks>()
        var weekFakeList = mutableListOf<Tasks>()
        var monthFakeList = mutableListOf<Tasks>()
        var restFakeList = mutableListOf<Tasks>()
        
        var todayWork = 0
        var todayLimitIU = 0
        var todayLimitINU = 0
        var todayLimitNIU = 0

        var tomorrowWork = 0
        var tomorrowLimitIU = 0
        var tomorrowLimitINU = 0
        var tomorrowLimitNIU = 0

        var trash = mutableListOf<Tasks>()

        //pobranie dzisiejszej daty
        val today = EasyDate(LocalDate.now())

        //przejscie po taskach waznych-pilnych (np, pozarach)
        for(i in tasks){
            if(i.importance == 1 && i.urgency == 1){

                val d = EasyDate(i.deadline)
                
                when{
                    //zadanie, które miało termin do dzisiaj, a się nie mieści, zostaje przesunięte na jutro, lub jeśli się nie da - do końca tyg
                    d.date == today.date -> {
                        when{
                            
                            todayWork + i.timeToFinish <= 60 -> {
                                todayLimitIU += 1
                                todayList.add(i)
                                todayWork += i.timeToFinish
                            }
                            
                            tomorrowWork + i.timeToFinish <= 60 -> {
                                tomorrowLimitIU += 1
                                tomorrowFakeList.add(i)
                                tomorrowWork += i.timeToFinish
                            }
                            
                            else -> weekFakeList.add(i)
                        }
                    }

                    //zadanie, które miało termin do jutra, a się nie mieści, zostaje przesunięte na do końca tyg
                    d.date == (today+1).date -> {
                        if(tomorrowWork + i.timeToFinish <= 60 ){ 
                            tomorrowLimitIU += 1
                            tomorrowFakeList.add(i)
                            tomorrowWork += i.timeToFinish
                        }
                        else {
                            weekFakeList.add(i)
                        }
                    }

                    d.date <= (today+7).date -> weekFakeList.add(i)

                    d.date <= (today+31).date -> monthFakeList.add(i)

                    else -> restFakeList.add(i)
                }
            }
        }


                //rozdzielenie tasków do odpowiednich przedziałów
        for (i in tasks){
                val d = EasyDate(i.deadline)
                when{

                    //zadanie, które miało termin do dzisiaj, a się nie mieści, zostaje przesunięte na jutro, lub jeśli się nie da - do końca tyg
                    d.date == today.date -> {
                        when{
                            todayWork + i.timeToFinish <= 60 -> {

                                when{
                                    i.importance == 1 && i.urgency == 1 -> todayLimitIU = 1
                                    i.importance == 1 && i.urgency == 0 -> todayLimitINU = 1
                                    i.importance == 0 && i.urgency == 1 && todayLimitNIU < 2 -> todayLimitNIU += 1
                                }

                                todayList.add(i)
                                todayWork += i.timeToFinish
                            }
                            tomorrowWork + i.timeToFinish <= 60 -> {

                                when{
                                    i.importance == 1 && i.urgency == 1 -> tomorrowLimitIU = 1
                                    i.importance == 1 && i.urgency == 0 -> tomorrowLimitINU = 1
                                    i.importance == 0 && i.urgency == 1 && tomorrowLimitNIU < 2 -> tomorrowLimitNIU += 1
                                }

                                tomorrowList.add(i)
                                tomorrowWork += i.timeToFinish
                            }
                            else -> weekList.add(i)
                        }
                    }

                    //zadanie, które miało termin do jutra, a się nie mieści, zostaje przesunięte na do końca tyg
                    d.date == (today+1).date -> {
                        if(tomorrowWork + i.timeToFinish <= 60 ){

                            when{
                                i.importance == 1 && i.urgency == 1 -> tomorrowLimitIU = 1
                                i.importance == 1 && i.urgency == 0 -> tomorrowLimitINU = 1
                                i.importance == 0 && i.urgency == 1 && tomorrowLimitNIU < 2 -> tomorrowLimitNIU += 1
                            }

                            tomorrowList.add(i)
                            tomorrowWork += i.timeToFinish
                        }
                        else {
                            weekList.add(i)
                        }
                    }

                    d.date <= (today+7).date -> weekList.add(i)

                    d.date <= (today+31).date -> monthList.add(i)

                    else -> restList.add(i)
                }

        }

        todayList.sortWith(compareBy({it.importance}, {it.urgency}, {it.timeToFinish}))
        tomorrowList.sortWith(compareBy({it.importance}, {it.urgency}, {it.timeToFinish}))
        weekList.sortWith(compareBy({it.importance}, {it.urgency}, {it.timeToFinish}))
        monthList.sortWith(compareBy({it.importance}, {it.urgency}, {it.timeToFinish}))
        restList.sortWith(compareBy({it.importance}, {it.urgency}, {it.timeToFinish}))

        todayList.reverse()
        tomorrowList.reverse()
        weekList.reverse()
        monthList.reverse()
        restList.reverse()
    }

}