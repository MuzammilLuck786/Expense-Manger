package com.pm.expensemanager.usermodel

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pm.expensemanager.usermodel.UserEntity

@Dao
interface UserDao {

    @Insert
    suspend fun insert(userEntity: UserEntity)

    @Query("SELECT * FROM userTable WHERE password = :password")
    suspend fun getUserByPassword(password: String): UserEntity?

    @Query("SELECT * FROM userTable WHERE email =:email")
    suspend fun getUserByEmail(email: String): UserEntity?

}