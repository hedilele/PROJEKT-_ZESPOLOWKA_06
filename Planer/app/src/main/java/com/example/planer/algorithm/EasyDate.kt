package com.example.planer.algorithm

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

/**
 * Klasa przechowuje daty jako inty
 * Posiada wiele konstruktorów, aby dało się różne typy dat sprowadzić to obiektów tej klasy
 * @param date - data jako int
 */
// TODO brakuje obsługi błędów i przykładów ekstremalnych
//(day * 1000000) + (month * 10000) + year          11042023
//day + (month * 100) + (year * 10000)    20230411
class EasyDate {

    var date : Int = 0

    /**
    Konstruktory
     */
    constructor(dateI : Int){
        date = dateI
    }

    constructor(day : Int, month: Int, year : Int){
        date += (year * 10000) + (month * 100) + day
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



    /**
    przeciążony getter
     */
    operator fun get(date2: EasyDate): EasyDate{
        return EasyDate(date2.date)
    }

    /**
    gettery od dnia, miesiąca i roku
     */
    fun getDay() : Int{
        return date % 100
    }

    fun getMonth() : Int{
        return (date / 100) % 100
    }

    fun getYear() : Int{
        return date / 10000
    }



    /**
     * Algorytm dodawania dowolnej ilości dni
     */
    operator fun plus(number : Int): EasyDate{
        var flag = 0
        var day = getDay()
        var month = getMonth()
        var year = getYear()
        var x = number

        if(day != 1){
            x += day-1
            day = 1
        }

        do{
            if(month < 8){
                if(month == 2){
                    if(year % 4 == 0){
                        if(x >= 29){
                            month += 1
                            x -= 29
                        }
                        else{
                            day += x
                            flag = 1
                        }
                    }
                    else{
                        if(x >= 28){
                            month += 1
                            x -= 28
                        }
                        else{
                            day += x
                            flag = 1
                        }
                    }
                }
                else if(month % 2 == 1){
                    if(x >= 31){
                        month += 1
                        x -= 31
                    }
                    else{
                        day += x
                        flag = 1
                    }
                }
                else if(month % 2 == 0){
                    if(x >= 30){
                        month += 1
                        x -= 30
                    }
                    else{
                        day += x
                        flag = 1
                    }
                }
            }
            else{
                if(month == 12){
                    if(x >= 31){
                        year += 1
                        month = 1
                        x -= 31
                    }
                    else{
                        day += x
                        flag = 1
                    }
                }
                else if(month % 2 == 0){
                    if(x >= 31){
                        month += 1
                        x -= 31
                    }
                    else{
                        day += x
                        flag = 1
                    }
                }
                else if(month % 2 == 1){
                    if(x >= 30){
                        month += 1
                        x -= 30
                    }
                    else{
                        day += x
                        flag = 1
                    }
                }
            }
            if(flag == 1)
                break
        }while (flag == 0);

        return EasyDate(day, month, year)
    }


    /**
     *  Konwertuje na int za pomocą funkcji convert, która koduje datę następująco:
     *       rok + miesiąc*10000 + dzień*1000000
     */
    //TODO uwzględnić jeszcze inne przypadki np z godziną
    fun convert(str: String): Int {
        var x = 0
        val day : Int
        val month : Int
        val year : Int
        val test = str[2]
        if(str.length == 10){
            if(test == '-' || test == '.'){
                day = str.substring(0, 2).toInt()
                month = str.substring(3, 5).toInt()
                year = str.substring(6).toInt()
            }
            else{
                day = str.substring(8).toInt()
                month = str.substring(5, 7).toInt()
                year = str.substring(0, 4).toInt()
            }
        }else if(str.length == 16){
            if(test == '-' || test == '.'){  //DD.MM.YYYY HH:mm
                day = str.substring(0, 2).toInt()
                month = str.substring(3, 5).toInt()
                year = str.substring(6, 10).toInt()
            }
            else{  //YYYY.MM.DD HH:mm
                year = str.substring(0, 4).toInt()
                month = str.substring(5, 7).toInt()
                day = str.substring(8, 10).toInt()
            }
        }
        else if(str.length == 17){
            if(test == '-' || test == '.'){  //DD.MM.YYYY  HH:mm
                day = str.substring(0, 2).toInt()
                month = str.substring(3, 5).toInt()
                year = str.substring(6, 10).toInt()
            }
            else{  //YYYY.MM.DD  HH:mm
                year = str.substring(0, 4).toInt()
                month = str.substring(5, 7).toInt()
                day = str.substring(8, 10).toInt()
            }
        }
        else{
            throw Exception("\n \nunknown format: $str \n length: ${str.length}")
        }

        x += day
        x += (month*100)
        x += (year*10000)

        return x
    }

    /**
     zwraca EasyDate jako String
     */
    fun to_String(): String{
        var temp = date
        var str = ""

        var r = temp % 100
        if(r < 10)
            str += "0"
        str += r.toString()

        temp /= 100
        str += "-"

        r = temp % 100
        if(r < 10)
            str += "0"
        str += r.toString()

        temp /= 100
        str += "-"

        r = temp
        str += r.toString()

        return str
    }

}