package com.pm.expensemanager.expensemodel

import androidx.lifecycle.LiveData
import com.pm.expensemanager.usermodel.UserEntity

class ExpenseRepository(private val incomeExpenseDao: IncomeExpenseDao) {

    suspend fun addExpenses(incomeExpenseEntity: IncomeExpenseEntity){
        incomeExpenseDao.insert(incomeExpenseEntity)
    }

    suspend fun updateExpense(incomeExpenseEntity: IncomeExpenseEntity){
        incomeExpenseDao.update(incomeExpenseEntity)
    }

    suspend fun deleteExpenseById(id:Long){
        incomeExpenseDao.deleteExpenseById(id)
    }

    fun getTotalIncome(status: String): LiveData<List<TotalAmount>> {
        return incomeExpenseDao.getTotalIncome(status)
    }

    fun getIncomeOrExpenseList(status: String): LiveData<List<IncomeExpenseEntity>> {
        return incomeExpenseDao.getIncomeOrExpenseList(status)
    }

    fun filterDate(date: String): LiveData<List<IncomeExpenseEntity>> {
        return incomeExpenseDao.filterDate(date)
    }


    val allIncomeExpenseList: LiveData<List<IncomeExpenseEntity>> = incomeExpenseDao.getAllIncomeExpenseList()

    }
