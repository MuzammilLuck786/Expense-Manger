package com.pm.expensemanager.expensemodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pm.expensemanager.usermodel.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpenseViewModel(private val expenseRepository: ExpenseRepository): ViewModel() {

    fun addExpenses(incomeExpenseEntity: IncomeExpenseEntity)=viewModelScope.launch(Dispatchers.IO){
        expenseRepository.addExpenses(incomeExpenseEntity)
    }


    fun updateExpense(incomeExpenseEntity: IncomeExpenseEntity)=viewModelScope.launch(Dispatchers.IO){
    expenseRepository.updateExpense(incomeExpenseEntity)
    }



    fun deleteExpenseById(id:Long)=viewModelScope.launch(Dispatchers.IO) { expenseRepository.deleteExpenseById(id) }

    fun getTotalIncome(status: String): LiveData<List<TotalAmount>> {
        return expenseRepository.getTotalIncome(status)
    }

    fun filterDate(date: String): LiveData<List<IncomeExpenseEntity>> {
        return expenseRepository.filterDate(date)
    }


    val allIncomeExpenseList: LiveData<List<IncomeExpenseEntity>> = expenseRepository.allIncomeExpenseList

    fun getIncomeOrExpenseList(status: String): LiveData<List<IncomeExpenseEntity>> {
        return expenseRepository.getIncomeOrExpenseList(status)
    }
}