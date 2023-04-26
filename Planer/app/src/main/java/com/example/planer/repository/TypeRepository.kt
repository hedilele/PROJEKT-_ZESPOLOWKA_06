package com.example.planer.repository

import androidx.lifecycle.LiveData
import com.example.planer.DAOs.TypeDAO
import com.example.planer.entities.Types

class TypeRepository(private val typeDAO: TypeDAO) {

    fun getAllTypes(): LiveData<List<Types>> {
        return typeDAO.getAllTypes()
    }

    suspend fun updateType(type: Types) {
        typeDAO.updateType(type)
    }

    suspend fun addType(type: Types) {
        typeDAO.insertType(type)
    }
}