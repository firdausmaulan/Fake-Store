package com.fd.fakestore.data.repository.user

import com.fd.fakestore.data.api.service.UserApiService
import com.fd.fakestore.data.local.preference.AppPreference
import com.fd.fakestore.data.model.User
import com.fd.fakestore.helper.JwtHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService,
    private val appPreference: AppPreference
) : IUserRepository {

    override suspend fun isAuthenticated(): Boolean {
        return appPreference.token.first() != null
    }

    override suspend fun login(username: String, password: String): Result<User> {
        val result = userApiService.login(username, password)
        if (result.isSuccess) {
            val user = result.getOrNull() ?: return Result.failure(Exception("User is null"))
            appPreference.saveToken(user.token ?: "")
            val userId : Int? = JwtHelper.getSubject(user.token ?: "")
            appPreference.saveUserId(userId ?: 0)
            return Result.success(user)
        } else {
            return Result.failure(result.exceptionOrNull() ?: Exception("Unknown Error"))
        }
    }

    override suspend fun getUserById(userId: Int): Result<User> {
        return userApiService.getUserById(userId)
    }

    override val token: Flow<String?> = appPreference.token

    override val userId: Flow<Int?> = appPreference.userId

    override suspend fun clearUserData() {
        appPreference.clearPreferences()
    }
}