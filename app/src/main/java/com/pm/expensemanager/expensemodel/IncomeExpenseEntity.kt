package com.pm.expensemanager.expensemodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "IncomeExpenseTable")
data class IncomeExpenseEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var amount: Double,
    var status: String,
    var reason:String,
    var description: String,
    var date: String
        ): Serializable

