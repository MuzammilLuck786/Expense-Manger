package com.pm.expensemanager.expensemodel

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pm.expensemanager.usermodel.DBHelperUser
import com.pm.expensemanager.usermodel.UserDao
import com.pm.expensemanager.usermodel.UserEntity

@Database(entities = [IncomeExpenseEntity::class], version = 1, exportSchema = false)
abstract class DBHelperExpense : RoomDatabase() {
    abstract fun expenseDao(): IncomeExpenseDao
    companion object {
        @Volatile
        private var INSTANCE: DBHelperExpense? = null

        fun getDB(context: Context): DBHelperExpense {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildRoomDB(context).also { INSTANCE = it }
            }
        }

        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                DBHelperExpense::class.java,
                "expenseDB"
            ).build()
    }
}