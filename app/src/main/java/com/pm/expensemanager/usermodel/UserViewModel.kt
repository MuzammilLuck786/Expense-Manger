package com.pm.expensemanager.usermodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val repository: UserRepository):ViewModel() {

    suspend fun insertUser(userEntity: UserEntity) {
            repository.addUser(userEntity)
    }

    suspend fun getUserByPassword(password: String): UserEntity? {
           return repository.getUserByPassword(password)
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        return repository.getUserByEmail(email)
    }

}