package com.example.planer

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.planer.DAOs.CalendarDAO
import com.example.planer.DAOs.HabitsDAO
import com.example.planer.DAOs.NotesDAO
import com.example.planer.DAOs.TasksDAO
import com.example.planer.entities.*

//Update wersji + 11, jesli zmienimy baze danych, dodamy jakies itp
@Database(entities = [Habits::class, Tasks::class, Finished::class, Subtasks::class, Calendar::class, Notes::class], version = 9)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitsDAO() : HabitsDAO
    abstract fun tasksDAO() : TasksDAO

    abstract fun notesDAO() : NotesDAO

    abstract fun calendarDAO() : CalendarDAO

    //TESTOWO

    //Singleton dla naszej bazy
    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            //Mozemy stworzyc tylko jeden taki obiekt - Patrz filmik - mozliwe,ze lepiej zrobione
            //Poprawic, zastanowic sie
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "TOP.sqlite"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }

        }

    }
}