package com.example.planer

import android.app.Application
import com.example.planer.AppDatabase

// potrzebne, aby dodać do manifestu
// ?????????
class DatabaseApp: Application() {
    val db by lazy{
        AppDatabase.getDatabase(this)
    }
}