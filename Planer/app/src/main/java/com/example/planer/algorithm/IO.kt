package com.example.planer.algorithm

import android.util.Log
import android.content.Context
import com.example.planer.entities.Habits
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
        var work = hours


        val isCreated = file.createNewFile()
        //plik zostal w tej chwili utworzony
        if(isCreated){
            file.appendText(today)
            file.appendText("\n$work")
            file.appendText("\n$work")
        }
        //plik juz istnial
        else{
            var data = file.readLines()
            if(data.size < 3){
                file.writeText(data[0])
                file.appendText("\n${data[1]}")
                file.appendText("\n$work")
            }

            data = file.readLines()

            //jeżeli mamy nowy dzień
            if(data[0] != today){
                file.writeText(today)
                file.appendText("\n$work")
                file.appendText("\n$work")
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
                }
                //jeżeli nowy limit godzin jest mniejszy od starego i dzisiejszej roboty
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

    //funkcja uzywana przy wyswietlaniu danych, ogranicza liste habitsow do tych nie odhaczonych
    //0 - nawyk do odhaczenia
    //1 - dzisiaj wykonano
    fun filterHabits(context: Context, habits: MutableList<Habits>): MutableList<Habits>{
        var list = habits
        val name = "habits.txt"
        val path = context.filesDir
        val file = File(path, name)
        val isCreated = file.createNewFile()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now().format(formatter)

        //jesli plik zostal dopiero co utworzony, to po dodaniu do niego daty, zapisze w nim wszystkie habitsy
        if(isCreated){
            file.appendText(today)
            for(i in list){
                file.appendText("\n${i.id} ${i.name}")
                file.appendText("\n0")
            }
        }
        //w przeciwnym razie...
        else{
            val data = file.readLines()
            val dateFromFile = EasyDate(data[0])
            val todayDate = EasyDate(today)

            //jeśli weszliśmy na apkę kolejny raz w ciągu dnia, to przechodzi po liscie habitsow do wyswietlenia i "usuwa" odhaczone
            if(dateFromFile.date == todayDate.date){
                val filteredHabits = mutableListOf<Habits>()
                for (i in list){
                    val str = "${i.id} ${i.name}"
                    if(data.contains(str)){
                        var position = data.indexOf(str)
                        position += 1
                        if(data[position] == "0")
                            filteredHabits.add(i)
                    }
                }
                list = filteredHabits
            }
            //jeśli mamy nowy dzień zawartosc pliku jest resetowana
            else{
                file.writeText(today)
                for(i in list){
                    file.appendText("\n${i.id} ${i.name}")
                    file.appendText("\n0")
                }
            }

        }

        return list
    }

    //dodaje habitsa do pliku
    fun addHabit(context: Context, habit: Habits){
        val name = "habits.txt"
        val path = context.filesDir
        val file = File(path, name)
        file.appendText("${habit.id} ${habit.name}\n0")
    }

    //obsluguje mechanizm odhaczenia nawyku
    fun habitDone(context: Context, habit: Habits){
        val name = "habits.txt"
        val path = context.filesDir
        val file = File(path, name)
        val data = file.readLines()
        val str = "${habit.id} ${habit.name}"
        var position = data.indexOf(str)
        position += 1
        var content = data[0]
        var i = 1;
        while (i < data.size){
            if(position == i)
                content += "\n1"
            else
                content += "\n${data[i]}"
            i++
        }
    }

    //usuwa habitsa z pliku
    fun deleteHabit(context: Context, habit: Habits){
        val name = "habits.txt"
        val path = context.filesDir
        val file = File(path, name)
        val data = file.readLines()
        val str = "${habit.id} ${habit.name}"
        var position = data.indexOf(str)
        var flag = 0
        var content = data[0]
        var i = 1;
        while (i < data.size){
            if(i != position)
                content += "\n${data[i]}"
            else{
                if(flag == 0){
                    position += 1
                    flag = 1
                }
            }
            i++
        }
    }
}