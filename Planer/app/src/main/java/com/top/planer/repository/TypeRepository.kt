package com.top.planer.repository

import androidx.lifecycle.LiveData
import com.top.planer.DAOs.TypeDAO
import com.top.planer.entities.Types

class TypeRepository(private val typeDAO: TypeDAO) {

    fun getAllTypes(): LiveData<List<Types>> {
        return typeDAO.getAllTypes()
    }

    fun getAllTypesList(): List<Types> {
        return typeDAO.getAllTypesList()
    }

    suspend fun updateType(type: Types) {
        typeDAO.updateType(type)
    }

    suspend fun updateTypes(types: List<Types>) {
        typeDAO.updateTypes(types)
    }

    suspend fun addType(type: Types) {
        typeDAO.insertType(type)
    }
}