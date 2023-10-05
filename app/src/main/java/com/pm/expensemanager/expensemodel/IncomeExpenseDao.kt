package com.pm.expensemanager.expensemodel

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface IncomeExpenseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(incomeExpenseEntity: IncomeExpenseEntity)

    @Update
    suspend fun update(incomeExpenseEntity: IncomeExpenseEntity)

    @Query("DELETE FROM IncomeExpenseTable WHERE id = :id")
    suspend fun deleteExpenseById(id: Long)

    @Query("SELECT * FROM IncomeExpenseTable")
    fun getAllIncomeExpenseList(): LiveData<List<IncomeExpenseEntity>>

    @Query("SELECT * FROM IncomeExpenseTable WHERE status = :status")
    fun getIncomeOrExpenseList(status: String): LiveData<List<IncomeExpenseEntity>>

    @Query("SELECT * FROM IncomeExpenseTable WHERE status = :status")
    fun getTotalIncome(status: String): LiveData<List<TotalAmount>>

    @Query("Select * From IncomeExpenseTable where date = :date")
    fun filterDate(date: String): LiveData<List<IncomeExpenseEntity>>

}