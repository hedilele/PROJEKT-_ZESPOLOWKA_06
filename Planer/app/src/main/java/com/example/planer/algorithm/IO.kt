package com.example.planer.algorithm

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class IO {

    //jeżeli data w pliku jest inna od dzisiejszej, to odświeża dane w pliku
    //w każdym przypadku na końcu zwraca liczbę godzin do przepracowania dziś
    fun newDay(context: Context): Int{
        val name = "data.txt"
        val path = context.filesDir
        val file = File(path, name)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now().format(formatter)
        val work: Int

        val isCreated = file.createNewFile()
        //plik zostal w tej chwili utworzony
        if(isCreated){
            file.appendText(today)
            file.appendText("\n60")
            work = 60
        }
        //plik juz istnial
        else{
            var data = file.readLines()
            if(data[0] != today){
                file.writeText(today)
                file.appendText("\n60")
            }
            data = file.readLines()
            work = data[1].toInt()
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
        file.writeText(data[0] + "\n" + work.toString())
    }
}