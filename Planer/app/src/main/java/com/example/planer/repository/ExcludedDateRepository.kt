package com.example.planer.repository

import androidx.lifecycle.LiveData
import com.example.planer.DAOs.ExcludedDateDAO
import com.example.planer.entities.ExcludedDate

class ExcludedDateRepository(private val excludedDateDAO: ExcludedDateDAO) {

    fun readExcludedDates(): LiveData<List<ExcludedDate>> {
        return excludedDateDAO.getExcludedDates()
    }

    suspend fun addExcludedDate(excludedDate: ExcludedDate) {
        excludedDateDAO.insert(excludedDate)
    }

    suspend fun removeExcludedDate(excludedDate: ExcludedDate) {
        excludedDateDAO.deleteByDate(excludedDate.excludedDate)
    }

    suspend fun removeDatesNotInList(excludedDates: List<ExcludedDate>) {
        excludedDateDAO.deleteDatesNotInList(excludedDates.map { it.excludedDate })
    }
}