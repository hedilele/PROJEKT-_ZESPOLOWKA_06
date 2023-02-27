package com.example.planer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.planer.entities.Habits
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    /*
    //Na ten moment tak nie dziala, musze potestowac TODO
    private var db = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "TOP.sqlite"
    ).build()
     */
    private lateinit var db : AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Tworzenie instancji tego
        db = AppDatabase.getDatabase(this)

        /*
        val addUserBtn = findViewById(R.id.button) as Button
        addUserBtn.setOnClickListener{
            addUser()
        }
         */
    }

    //Operacje rozne na bazie, zapis, update itp odpalamy w innym watku, nie glownym
    //Testowa funkcja do insertowania
    /*
    fun addUser()
    {
        val habits = Habits(null, 1, 10)
        GlobalScope.launch(Dispatchers.IO)
        {
            db.habitsDAO().insert(habits)
        }
    }
     */
}