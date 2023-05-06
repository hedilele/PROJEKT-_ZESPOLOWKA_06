package com.example.planer.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.planer.AppDatabase
import com.example.planer.entities.Types
import com.example.planer.repository.TypeRepository


//Referencja do aplikacji
class TypeViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<Types>>
    private val repository: TypeRepository

    //To zawsze pierwsze bedzie sie wykonywalo kiedy callujemy UserViewModel
    init {
        val typeDAO = AppDatabase.getDatabase(application).typesDAO()
        repository = TypeRepository(typeDAO)
        readAllData = repository.getAllTypes()
    }


}