package com.pm.expensemanager.usermodel

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {

    suspend fun addUser(userEntity: UserEntity){
        userDao.insert(userEntity)
    }

    suspend fun getUserByEmail(email:String): UserEntity?{
        return userDao.getUserByEmail(email)
    }

    suspend fun getUserByPassword(password:String): UserEntity?{
        return userDao.getUserByPassword(password)
    }
}