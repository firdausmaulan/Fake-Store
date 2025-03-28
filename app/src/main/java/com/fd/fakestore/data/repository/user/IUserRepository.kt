package com.fd.fakestore.data.repository.user

import com.fd.fakestore.data.model.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {

    suspend fun isAuthenticated(): Boolean
    suspend fun login(username: String, password: String): Result<User>
    suspend fun getUserData(): Result<User>
    val token: Flow<String?>
    val userId: Flow<Int?>
    suspend fun clearUserData()

}