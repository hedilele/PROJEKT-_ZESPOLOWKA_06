package com.example.planer

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.planer.DAOs.HabitsDAO
import com.example.planer.entities.Habits

@Database(entities = [Habits::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitsDAO() : HabitsDAO

    //TESTOWO

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "TOP.sqlite"
                ).build()
                INSTANCE = instance
                return instance
            }

        }

    }
}