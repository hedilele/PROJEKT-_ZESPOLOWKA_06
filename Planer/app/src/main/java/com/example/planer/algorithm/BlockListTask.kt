package com.example.planer.algorithm

import android.content.Context
import com.example.planer.entities.Tasks
import java.time.LocalDate

/**
 * Klasa, która rozdziela listę tasków na poszczególne przedziały czasowe.
 */
class BlockListTask (
    var list : List<Tasks>,
    var context : Context
){
    val io = IO()
    private val tasks = list
    var TODAY_WORK = io.newDay(context)
    var TOMORROW_WORK = 60
    var todayList = mutableListOf<Tasks>()
    var tomorrowList = mutableListOf<Tasks>()
    var weekList = mutableListOf<Tasks>()
    var monthList = mutableListOf<Tasks>()
    var restList = mutableListOf<Tasks>()


    /**
     * Funkcja układająca plan użytkownikowi
     */
    fun planner(){

        val deadlines = mutableSetOf<Tasks>()       //zbiór tasków do określonego dnia
        val myDates = mutableSetOf<Tasks>()         //zbiór tasków o konkretnym dniu wykonania

        val red = mutableSetOf<Tasks>()     //ważne-pilne
        val green = mutableSetOf<Tasks>()   //ważne-niepilne
        val yellow = mutableSetOf<Tasks>()  //nieważne-pilne
        val blue = mutableSetOf<Tasks>()    //nieważne-niepilne

        val tomorrowFakeList = mutableSetOf<Tasks>()
        val weekFakeList = mutableSetOf<Tasks>()
        val monthFakeList = mutableSetOf<Tasks>()
        val restFakeList = mutableSetOf<Tasks>()
        
        var todayWork = 0
        var todayLimitIU = 0
        var todayLimitINU = 0
        var todayLimitNIU = 0

        var tomorrowWork = 0
        var tomorrowLimitIU = 0
        var tomorrowLimitINU = 0
        var tomorrowLimitNIU = 0

        //pobranie dzisiejszej daty
        val today = EasyDate(LocalDate.now())

        //dziel taski na "do dnia" i "konkretny dzien"
        for(i in tasks){
            if(i.date == null){
                val d = EasyDate(i.deadline)
                if(i.importance == 1 && i.urgency == 0 && d.date < (today+3).date)
                    i.urgency = 1
                deadlines.add(i)
            }
            else
                myDates.add(i)
        }

        //przypisanie tasków o konkretnym dniu realizacji do odpowiednich list
        for(i in myDates){
            val d = EasyDate(i.deadline)
            when{
                d.date == today.date -> {
                    todayWork += i.timeToFinish
                    todayList.add(i)
                }
                d.date == (today+1).date -> {
                    tomorrowWork += i.timeToFinish
                    tomorrowList.add(i)
                }
                d.date < (today+7).date -> {
                    weekList.add(i)
                }
                d.date < (today+30).date -> {
                    monthList.add(i)
                }
                else -> restList.add(i)
            }
        }

        //przydziel taski o określonym deadlinie, do odpowiednich ćwiartek
        for(i in deadlines){
            when{
                i.importance == 1 && i.urgency == 1 -> red.add(i)
                i.importance == 1 && i.urgency == 0 -> green.add(i)
                i.importance == 0 && i.urgency == 1 -> yellow.add(i)
                else -> blue.add(i)
            }
        }



        //przejscie po taskach waznych-pilnych (np, pozarach)
        for(i in red){
                val d = EasyDate(i.deadline)
                
                when{
                    //zadanie, które miało termin na max 3 najbliższe dni włącznie z dzisiaj
                    d.date <= (today+2).date -> {
                        //zostanie dodane na dzisiaj, jutro lub do konca tygodnia zaleznie od limitu obciazenia (TODAY_WORK)
                        when{
                            
                            todayWork + i.timeToFinish <= TODAY_WORK -> {
                                todayLimitIU += 1
                                todayList.add(i)
                                todayWork += i.timeToFinish
                            }
                            
                            tomorrowWork + i.timeToFinish <= TOMORROW_WORK -> {
                                tomorrowLimitIU += 1
                                tomorrowList.add(i)
                                tomorrowWork += i.timeToFinish
                            }
                            
                            else -> weekFakeList.add(i)
                        }
                    }

                    d.date <= (today+7).date -> weekFakeList.add(i)

                    d.date <= (today+31).date -> monthFakeList.add(i)

                    else -> restFakeList.add(i)
                }
            }

        //jeśli mamy jakieś odległe pilne-ważne zadanie zostanie ono wypchnięte na dzisiaj o ile jest mozliwosc
        if(todayLimitIU == 0){
            when{
                weekFakeList.isNotEmpty() -> {
                    val task = weekFakeList.first()
                    todayList.add(task)
                    todayWork += task.timeToFinish
                    weekFakeList.remove(task)
                }
                monthFakeList.isNotEmpty() -> {
                    val task = monthFakeList.first()
                    todayList.add(task)
                    todayWork += task.timeToFinish
                    monthFakeList.remove(task)
                }
                restFakeList.isNotEmpty() -> {
                    val task = restFakeList.first()
                    todayList.add(task)
                    todayWork += task.timeToFinish
                    restFakeList.remove(task)
                }
            }
        }

        //jeśli mamy jakieś odległe pilne-ważne zadanie zostanie ono wypchnięte na jutro o ile jest mozliwosc
        if(tomorrowLimitIU == 0){
            when{
                weekFakeList.isNotEmpty() -> {
                    val task = weekFakeList.first()
                    tomorrowList.add(task)
                    tomorrowWork += task.timeToFinish
                    weekFakeList.remove(task)
                }
                monthFakeList.isNotEmpty() -> {
                    val task = monthFakeList.first()
                    tomorrowList.add(task)
                    tomorrowWork += task.timeToFinish
                    monthFakeList.remove(task)
                }
                restFakeList.isNotEmpty() -> {
                    val task = restFakeList.first()
                    tomorrowList.add(task)
                    tomorrowWork += task.timeToFinish
                    restFakeList.remove(task)
                }
            }
        }

        //zatwierdzenie pozostałych ważnych-pilnych
        while(weekFakeList.isNotEmpty()){
            weekList.add(weekFakeList.first())
            weekFakeList.remove(weekFakeList.first())
        }

        while(monthFakeList.isNotEmpty()){
            monthList.add(monthFakeList.first())
            monthFakeList.remove(monthFakeList.first())
        }

        while(restFakeList.isNotEmpty()){
            restList.add(restFakeList.first())
            restFakeList.remove(restFakeList.first())
        }



        //przejscie po taskach waznych-NIEpilnych (np, celach)
        for(i in green){

                val d = EasyDate(i.deadline)

                when{
                    //zadanie, które miało termin do dzisiaj
                    d.date <= today.date -> {
                        //zostanie dodane na dzisiaj, jutro lub do konca tygodnia zaleznie od limitu obciazenia (TODAY_WORK)
                        when{

                            todayWork + i.timeToFinish <= TODAY_WORK -> {
                                todayLimitINU += 1
                                todayList.add(i)
                                todayWork += i.timeToFinish
                            }

                            tomorrowWork + i.timeToFinish <= TOMORROW_WORK -> {
                                tomorrowLimitINU += 1
                                tomorrowFakeList.add(i)
                                tomorrowWork += i.timeToFinish
                            }

                            else -> weekFakeList.add(i)
                        }
                    }

                    d.date == (today+1).date -> {
                        when{
                            tomorrowWork + i.timeToFinish <= TOMORROW_WORK -> {
                                tomorrowLimitINU += 1
                                tomorrowFakeList.add(i)
                                tomorrowWork += i.timeToFinish
                            }

                            else -> weekFakeList.add(i)
                        }
                    }

                    d.date <= (today+7).date -> weekFakeList.add(i)

                    d.date <= (today+31).date -> monthFakeList.add(i)

                    else -> restFakeList.add(i)
                }
            }

        //jeśli mamy jakieś odległe pilne-NIEważne zadanie zostanie ono wypchnięte na dzisiaj o ile jest mozliwosc
        if(todayLimitINU == 0){
            when{
                tomorrowFakeList.isNotEmpty() && todayWork + tomorrowFakeList.first().timeToFinish <= TODAY_WORK -> {
                    val task = tomorrowFakeList.first()
                    todayList.add(task)
                    todayWork += task.timeToFinish
                    tomorrowWork -= task.timeToFinish
                    tomorrowFakeList.remove(task)
                }
                weekFakeList.isNotEmpty() &&  todayWork + weekFakeList.first().timeToFinish <= TODAY_WORK -> {
                    val task = weekFakeList.first()
                    todayList.add(task)
                    todayWork += task.timeToFinish
                    weekFakeList.remove(task)
                }
                monthFakeList.isNotEmpty() &&  todayWork + monthFakeList.first().timeToFinish <= TODAY_WORK -> {
                    val task = monthFakeList.first()
                    todayList.add(task)
                    todayWork += task.timeToFinish
                    monthFakeList.remove(task)
                }
                restFakeList.isNotEmpty() &&  todayWork + restFakeList.first().timeToFinish <= TODAY_WORK -> {
                    val task = restFakeList.first()
                    todayList.add(task)
                    todayWork += task.timeToFinish
                    restFakeList.remove(task)
                }
            }
        }

        //jeśli mamy jakieś odległe pilne-NIEważne zadanie zostanie ono wypchnięte na jutro o ile jest mozliwosc
        if(tomorrowLimitINU == 0){
            when{
                weekFakeList.isNotEmpty() && tomorrowWork + weekFakeList.first().timeToFinish <= TOMORROW_WORK -> {
                    val task = weekFakeList.first()
                    tomorrowList.add(task)
                    tomorrowWork += task.timeToFinish
                    weekFakeList.remove(task)
                }
                monthFakeList.isNotEmpty() && tomorrowWork + monthFakeList.first().timeToFinish <= TOMORROW_WORK -> {
                    val task = monthFakeList.first()
                    tomorrowList.add(task)
                    tomorrowWork += task.timeToFinish
                    monthFakeList.remove(task)
                }
                restFakeList.isNotEmpty() && tomorrowWork + restFakeList.first().timeToFinish <= TOMORROW_WORK -> {
                    val task = restFakeList.first()
                    tomorrowList.add(task)
                    tomorrowWork += task.timeToFinish
                    restFakeList.remove(task)
                }
            }
        }

        //zatwierdzenie pozostałych ważnych-NIEpilnych
        while(tomorrowFakeList.isNotEmpty()){
            tomorrowList.add(tomorrowFakeList.first())
            tomorrowFakeList.remove(tomorrowFakeList.first())
        }
        
        while(weekFakeList.isNotEmpty()){
            weekList.add(weekFakeList.first())
            weekFakeList.remove(weekFakeList.first())
        }

        while(monthFakeList.isNotEmpty()){
            monthList.add(monthFakeList.first())
            monthFakeList.remove(monthFakeList.first())
        }

        while(restFakeList.isNotEmpty()){
            restList.add(restFakeList.first())
            restFakeList.remove(restFakeList.first())
        }



        //przejscie po taskach NIEwaznych-pilnych (np, lodówka się pali)
        for(i in yellow){

                val d = EasyDate(i.deadline)

                when{
                    //zadanie, które miało termin do dzisiaj
                    d.date <= today.date -> {
                        //zostanie dodane na dzisiaj, jutro lub do konca tygodnia zaleznie od limitu obciazenia (TODAY_WORK)
                        when{

                            todayWork + i.timeToFinish <= TODAY_WORK -> {
                                todayLimitNIU += 1
                                todayList.add(i)
                                todayWork += i.timeToFinish
                            }

                            tomorrowWork + i.timeToFinish <= TOMORROW_WORK -> {
                                tomorrowLimitNIU += 1
                                tomorrowFakeList.add(i)
                                tomorrowWork += i.timeToFinish
                            }

                            else -> weekFakeList.add(i)
                        }
                    }

                    d.date == (today+1).date -> {
                        when{
                            tomorrowWork + i.timeToFinish <= TOMORROW_WORK -> {
                                tomorrowLimitNIU += 1
                                tomorrowFakeList.add(i)
                                tomorrowWork += i.timeToFinish
                            }

                            else -> weekFakeList.add(i)
                        }
                    }

                    d.date <= (today+7).date -> weekFakeList.add(i)

                    d.date <= (today+31).date -> monthFakeList.add(i)

                    else -> restFakeList.add(i)
                }
        }

        //jeśli mamy jakieś odległe NIEpilne-ważne zadanie zostanie ono wypchnięte na dzisiaj o ile jest mozliwosc
        while(todayLimitNIU < 2){
            when{
                tomorrowFakeList.isNotEmpty() && todayWork + tomorrowFakeList.first().timeToFinish <= TODAY_WORK -> {
                    val task = tomorrowFakeList.first()
                    todayList.add(task)
                    todayWork += task.timeToFinish
                    tomorrowWork -= task.timeToFinish
                    todayLimitNIU += 1
                    tomorrowLimitNIU -= 1
                    tomorrowFakeList.remove(task)
                }
                weekFakeList.isNotEmpty() &&  todayWork + weekFakeList.first().timeToFinish <= TODAY_WORK -> {
                    val task = weekFakeList.first()
                    todayList.add(task)
                    todayWork += task.timeToFinish
                    todayLimitNIU += 1
                    weekFakeList.remove(task)
                }
                monthFakeList.isNotEmpty() &&  todayWork + monthFakeList.first().timeToFinish <= TODAY_WORK -> {
                    val task = monthFakeList.first()
                    todayList.add(task)
                    todayWork += task.timeToFinish
                    todayLimitNIU += 1
                    monthFakeList.remove(task)
                }
                restFakeList.isNotEmpty() &&  todayWork + restFakeList.first().timeToFinish <= TODAY_WORK -> {
                    val task = restFakeList.first()
                    todayList.add(task)
                    todayWork += task.timeToFinish
                    todayLimitNIU += 1
                    restFakeList.remove(task)
                }
                else -> break
            }
        }

        //jeśli mamy jakieś odległe NIEpilne-ważne zadanie zostanie ono wypchnięte na jutro o ile jest mozliwosc
        while(tomorrowLimitNIU < 2){
            when{
                weekFakeList.isNotEmpty() && tomorrowWork + weekFakeList.first().timeToFinish <= TOMORROW_WORK -> {
                    val task = weekFakeList.first()
                    tomorrowList.add(task)
                    tomorrowWork += task.timeToFinish
                    tomorrowLimitNIU += 1
                    weekFakeList.remove(task)
                }
                monthFakeList.isNotEmpty() && tomorrowWork + monthFakeList.first().timeToFinish <= TOMORROW_WORK -> {
                    val task = monthFakeList.first()
                    tomorrowList.add(task)
                    tomorrowWork += task.timeToFinish
                    tomorrowLimitNIU += 1
                    monthFakeList.remove(task)
                }
                restFakeList.isNotEmpty() && tomorrowWork + restFakeList.first().timeToFinish <= TOMORROW_WORK -> {
                    val task = restFakeList.first()
                    tomorrowList.add(task)
                    tomorrowWork += task.timeToFinish
                    tomorrowLimitNIU += 1
                    restFakeList.remove(task)
                }
                else -> break
            }
        }

        //zatwierdzenie pozostałych NIEważnych-pilnych
        while(tomorrowFakeList.isNotEmpty()){
            tomorrowList.add(tomorrowFakeList.first())
            tomorrowFakeList.remove(tomorrowFakeList.first())
        }

        while(weekFakeList.isNotEmpty()){
            weekList.add(weekFakeList.first())
            weekFakeList.remove(weekFakeList.first())
        }

        while(monthFakeList.isNotEmpty()){
            monthList.add(monthFakeList.first())
            monthFakeList.remove(monthFakeList.first())
        }

        while(restFakeList.isNotEmpty()){
            restList.add(restFakeList.first())
            restFakeList.remove(restFakeList.first())
        }



        //przejscie po taskach NIEwaznych-NIEpilnych (np, pierdoły)
        for(i in blue){

                val d = EasyDate(i.deadline)

                when{
                    //zadanie, które miało termin do dzisiaj
                    d.date <= today.date -> {
                        //zostanie dodane na dzisiaj, jutro lub do konca tygodnia zaleznie od limitu obciazenia (TODAY_WORK)
                        when{

                            todayWork + i.timeToFinish <= TODAY_WORK -> {
                                todayLimitNIU += 1
                                todayList.add(i)
                                todayWork += i.timeToFinish
                            }

                            tomorrowWork + i.timeToFinish <= TOMORROW_WORK -> {
                                tomorrowLimitNIU += 1
                                tomorrowFakeList.add(i)
                                tomorrowWork += i.timeToFinish
                            }

                            else -> weekFakeList.add(i)
                        }
                    }

                    d.date == (today+1).date -> {
                        when{
                            tomorrowWork + i.timeToFinish <= TOMORROW_WORK -> {
                                tomorrowLimitNIU += 1
                                tomorrowFakeList.add(i)
                                tomorrowWork += i.timeToFinish
                            }

                            else -> weekFakeList.add(i)
                        }
                    }

                    d.date <= (today+7).date -> weekFakeList.add(i)

                    d.date <= (today+31).date -> monthFakeList.add(i)

                    else -> restFakeList.add(i)
                }
        }

        //jeśli mamy jakieś odległe NIEpilne-NIEważne zadanie zostanie ono wypchnięte na dzisiaj o ile jest mozliwosc
        while(todayWork < TODAY_WORK){
            when{
                tomorrowFakeList.isNotEmpty() && todayWork + tomorrowFakeList.first().timeToFinish <= TODAY_WORK -> {
                    val task = tomorrowFakeList.first()
                    todayList.add(task)
                    todayWork += task.timeToFinish
                    tomorrowWork -= task.timeToFinish
                    tomorrowFakeList.remove(task)
                }
                weekFakeList.isNotEmpty() &&  todayWork + weekFakeList.first().timeToFinish <= TODAY_WORK -> {
                    val task = weekFakeList.first()
                    todayList.add(task)
                    todayWork += task.timeToFinish
                    weekFakeList.remove(task)
                }
                monthFakeList.isNotEmpty() &&  todayWork + monthFakeList.first().timeToFinish <= TODAY_WORK -> {
                    val task = monthFakeList.first()
                    todayList.add(task)
                    todayWork += task.timeToFinish
                    monthFakeList.remove(task)
                }
                restFakeList.isNotEmpty() &&  todayWork + restFakeList.first().timeToFinish <= TODAY_WORK -> {
                    val task = restFakeList.first()
                    todayList.add(task)
                    todayWork += task.timeToFinish
                    restFakeList.remove(task)
                }
                else -> break
            }
        }

        //jeśli mamy jakieś odległe NIEpilne-NIEważne zadanie zostanie ono wypchnięte na jutro o ile jest mozliwosc
        while(tomorrowWork < TODAY_WORK){
            when{
                weekFakeList.isNotEmpty() && tomorrowWork + weekFakeList.first().timeToFinish <= TOMORROW_WORK -> {
                    val task = weekFakeList.first()
                    tomorrowList.add(task)
                    tomorrowWork += task.timeToFinish
                    weekFakeList.remove(task)
                }
                monthFakeList.isNotEmpty() && tomorrowWork + monthFakeList.first().timeToFinish <= TOMORROW_WORK -> {
                    val task = monthFakeList.first()
                    tomorrowList.add(task)
                    tomorrowWork += task.timeToFinish
                    monthFakeList.remove(task)
                }
                restFakeList.isNotEmpty() && tomorrowWork + restFakeList.first().timeToFinish <= TOMORROW_WORK -> {
                    val task = restFakeList.first()
                    tomorrowList.add(task)
                    tomorrowWork += task.timeToFinish
                    restFakeList.remove(task)
                }
                else -> break
            }
        }


        //zatwierdzenie pozostałych NIEważnych-NIEpilnych
        while(tomorrowFakeList.isNotEmpty()){
            tomorrowList.add(tomorrowFakeList.first())
            tomorrowFakeList.remove(tomorrowFakeList.first())
        }

        while(weekFakeList.isNotEmpty()){
            weekList.add(weekFakeList.first())
            weekFakeList.remove(weekFakeList.first())
        }

        while(monthFakeList.isNotEmpty()){
            monthList.add(monthFakeList.first())
            monthFakeList.remove(monthFakeList.first())
        }

        while(restFakeList.isNotEmpty()){
            restList.add(restFakeList.first())
            restFakeList.remove(restFakeList.first())
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