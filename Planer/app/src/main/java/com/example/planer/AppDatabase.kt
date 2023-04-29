package com.example.planer

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.planer.DAOs.*
import com.example.planer.entities.*
import com.example.planer.entities.typeconverters.LocalDateTypeConverter

//Update wersji + 11, jesli zmienimy baze danych, dodamy jakies itp
@Database(
    entities =
    [
        Habits::class,
        Tasks::class,
        Finished::class,
        Subtasks::class,
        Calendar::class,
        Notes::class,
        Settings::class,
        ExcludedDate::class,
        LastAccess::class,
        Types::class
    ],
    version = 17)
@TypeConverters(LocalDateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitsDAO() : HabitsDAO
    abstract fun tasksDAO() : TasksDAO
    abstract fun settingsDAO() : SettingsDAO
    abstract fun excludedDateDAO() : ExcludedDateDAO
    abstract fun lastAccessDAO() : LastAccessDAO
    abstract fun notesDAO() : NotesDAO
    abstract fun calendarDAO() : CalendarDAO
    abstract fun typesDAO() : TypeDAO

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
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                return instance
            }

        }

    }
}