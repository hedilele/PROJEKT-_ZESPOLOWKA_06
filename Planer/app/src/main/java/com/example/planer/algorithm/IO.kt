package com.example.planer.algorithm

import android.util.Log
import android.content.Context
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class IO {

    //jeżeli data w pliku jest inna od dzisiejszej, to odświeża dane w pliku
    //w każdym przypadku na końcu zwraca liczbę godzin do przepracowania dziś
    /*
    5min 10min 30min 1h 2h 2h+ 3h 4h 5h 6h 7h 8h
       1	2    6    12 24 30 36 48 60 72 84 96
     */
    fun newDay(context: Context, hours: Int): Int{
        val name = "data.txt"
        val path = context.filesDir
        val file = File(path, name)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now().format(formatter)
        var work = 60

        when (hours) {
            1 -> work = 12
            2 -> work = 24
            3 -> work = 36
            4 -> work = 48
            5 -> work = 60
            6 -> work = 72
            7 -> work = 84
            8 -> work = 96
        }

        val isCreated = file.createNewFile()
        //plik zostal w tej chwili utworzony
        if(isCreated){
            file.appendText(today)
            file.appendText("\n$work")
            file.appendText("\n$work")

            Log.d("work", "utworzon, do odrobienia: $work")
        }
        //plik juz istnial
        else{
            val data = file.readLines()
            //jeżeli mamy nowy dzień
            if(data[0] != today){
                file.writeText(today)
                file.appendText("\n$work")
                file.appendText("\n$work")
                Log.d("work", "nowy dzien, do odrobienia: $work")
            }
            //jeżeli w pliku nie ma linii z zapamiętanym limitem
            else if(data[2].toInt() != work){
                file.writeText(today)
                val done = data[1].toInt()
                //jeżeli się dzisiaj narobiliśmy
                if(done <= work) {
                    file.appendText("\n$done")
                    file.appendText("\n$work")
                    work = done
                    Log.d("work", "nowy limit, ale cos robilem, do odrobienia: $work")
                }
                //jeżeli nowy limit godzin jest mniejszy od starego i dzisiejszej roboty
                else {
                    file.appendText("\n$work")
                    file.appendText("\n$work")
                    Log.d("work", "nowy limit, ale nic nie robilem lub zmniejszylem, do odrobienia: $work")
                }
            }
            else{
                work = data[1].toInt()
                Log.d("work", "limit bez zmian, do odrobienia: $work")
            }
        }
        return work
    }

    //zmniejsza liczbe godzin do przepracowania w pliku
    fun updateWork(context: Context, workTime: Int){
        val name = "data.txt"
        val path = context.filesDir
        val file = File(path, name)
        val data = file.readLines()
        var work = data[1].toInt()
        work -= workTime
        file.writeText(data[0] + "\n" + work.toString() + "\n" +data[2])
    }
}