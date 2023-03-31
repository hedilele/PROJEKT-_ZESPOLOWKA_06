package com.example.planer.algorithm

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

/**
 * Klasa przechowuje daty jako inty
 * Posiada wiele konstruktorów, aby dało się różne typy dat sprowadzić to obiektów tej klasy
 *
 * @param date - data jako int
 */
class EasyDate {

    public var date : Int = 0

    constructor(dateI : Int){
        date = dateI
    }

    constructor(dateD : Date){

    }

    constructor(dateL : LocalDate){
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val str = dateL.format(formatter)
        date = convert(str)
    }

    constructor(dateS : String){
        date = convert(dateS)
    }

    operator fun get(date2: EasyDate): EasyDate{
        return EasyDate(date2.date)
    }

    operator fun plus(number : Int): EasyDate{
        return EasyDate(date + number)

    }



    /**
     *  Konwertuje na int za pomocą funkcji convert, która koduje datę następująco:
     *       rok + miesiąc*10000 + dzień*1000000
     */
    private fun convert(str: String): Int {
        var x = 0
        val day = str.substring(8).toInt()
        val month = str.substring(5, 7).toInt()
        val year = str.substring(0, 4).toInt()

        x += (day*1000000)
        x += (month*10000)
        x += year

        return x
    }

    /**
     zwraca EasyDate jako String
     */
    fun to_String(): String{
        var temp = date
        var str = ""

        var r = temp % 10000
        str += r.toString()

        temp /= 10000
        str += "-"

        r = temp % 100
        if(r < 10)
            str += "0"
        str += r.toString()

        temp /= 100
        str += "-"

        r = temp
        if(r < 10)
            str += "0"
        str += r.toString()

        return str
    }

}