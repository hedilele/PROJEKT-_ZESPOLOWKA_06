package com.example.planer.repository

import androidx.lifecycle.LiveData
import com.example.planer.DAOs.LastAccessDAO
import com.example.planer.entities.LastAccess
import java.time.LocalDate

class LastAccessRepository(private val lastAccessDAO: LastAccessDAO) {

    fun getLastAccessDate(): LiveData<LastAccess> {
        return lastAccessDAO.getLastAccess()
    }

    suspend fun updateAccessDate(date: LocalDate) {
        lastAccessDAO.update(LastAccess(1, date))
    }
    suspend fun createLastAccess(date: LocalDate) {
        lastAccessDAO.insert(LastAccess(1, date))
    }
}