package com.example.planer.algorithm

import android.annotation.SuppressLint
import android.content.Context
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class IO {

    //zwraca ilość godzin do przepracowania dziś
    fun readWork(){

    }

    //jeżeli data w pliku jest inna od dzisiejszej, to odświeża dane w pliku
    fun newDay(context: Context){
        val name = "data.txt"
        val path = context.filesDir
        val file = File(path, name)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now().format(formatter)

        var isCreated = file.createNewFile()
        //plik zostal w tej chwili utworzony
        if(isCreated){
            file.appendText(today)
            file.appendText("60")
        }
        //plik juz istnial
        else{
            val data = file.readLines()
            if(data[0] != today){
                file.writeText(today)
                file.appendText("60")
            }
        }
    }

    //zmniejsza liczbe godzin do przepracowania w pliku
    fun updateWork(){

    }

    //tworzy plik z dzisiejsza data i godzinami pracy
    fun createData(){

    }
}