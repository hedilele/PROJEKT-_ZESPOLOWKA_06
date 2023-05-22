package com.top.planer.algorithm

import android.content.Context
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Klasa odpowiedzialna za mechanizm odhaczania tasków.
 * Posługuje się plikiem w tym celu.
 */

class IO {

    /**jeżeli data w pliku jest inna od dzisiejszej, to odświeża dane w pliku
    w każdym przypadku na końcu zwraca liczbę godzin do przepracowania dziś

    5min 10min 30min 1h 2h 2h+ 3h 4h 5h 6h 7h 8h
       1	2    6    12 24 30 36 48 60 72 84 96
     */
    fun newDay(context: Context, hours: Int): Int{
        val name = "data.txt"
        val path = context.filesDir
        val file = File(path, name)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now().format(formatter)
        var work = hours


        val isCreated = file.createNewFile()
        if(isCreated){
            file.appendText(today)
            file.appendText("\n$work")
            file.appendText("\n$work")
        }
        else{
            var data = file.readLines()
            if(data.size < 3){
                file.writeText(data[0])
                file.appendText("\n${data[1]}")
                file.appendText("\n$work")
            }

            data = file.readLines()

            if(data[0] != today){
                file.writeText(today)
                file.appendText("\n$work")
                file.appendText("\n$work")
            }
            else if(data[2].toInt() != work){
                file.writeText(today)
                val done = data[1].toInt()
                if(done <= work) {
                    file.appendText("\n$done")
                    file.appendText("\n$work")
                    work = done
                }
                else {
                    file.appendText("\n$work")
                    file.appendText("\n$work")
                }
            }
            else{
                work = data[1].toInt()
            }
        }
        return work
    }

    /**
     * zmniejsza liczbe godzin do przepracowania w pliku
     */
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