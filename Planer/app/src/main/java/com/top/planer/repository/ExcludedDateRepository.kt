package com.top.planer.repository

import androidx.lifecycle.LiveData
import com.top.planer.DAOs.ExcludedDateDAO
import com.top.planer.entities.ExcludedDate

class ExcludedDateRepository(private val excludedDateDAO: ExcludedDateDAO) {

    fun readExcludedDates(): LiveData<MutableList<ExcludedDate>> {
        return excludedDateDAO.getExcludedDates()
    }

    fun getExcludedDatesList(): List<ExcludedDate> {
        return excludedDateDAO.getExcludedDatesList()
    }

    suspend fun addExcludedDate(excludedDate: ExcludedDate) {
        excludedDateDAO.insert(excludedDate)
    }

    suspend fun addExcludedDates(excludedDates: List<ExcludedDate>) {
        excludedDateDAO.insertDates(excludedDates)
    }

    suspend fun removeExcludedDate(excludedDate: ExcludedDate) {
        excludedDateDAO.deleteByDate(excludedDate.excludedDate)
    }

    suspend fun removeDatesNotInList(excludedDates: List<ExcludedDate>) {
        excludedDateDAO.deleteDatesNotInList(excludedDates.map { it.excludedDate })
    }
}