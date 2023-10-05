package com.pm.expensemanager.usermodel

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class DBHelperUser : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: DBHelperUser? = null

        fun getDB(context: Context): DBHelperUser {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildRoomDB(context).also { INSTANCE = it }
            }
        }

        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                DBHelperUser::class.java,
                "userDB"
            ).build()
    }
}