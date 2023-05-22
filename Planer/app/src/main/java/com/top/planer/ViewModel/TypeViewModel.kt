package com.top.planer.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.top.planer.AppDatabase
import com.top.planer.entities.Types
import com.top.planer.repository.TypeRepository


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