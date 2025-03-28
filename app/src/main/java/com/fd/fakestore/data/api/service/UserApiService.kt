package com.fd.fakestore.data.api.service

import com.fd.fakestore.data.api.core.AppHttpClient
import com.fd.fakestore.data.model.User

class UserApiService {

    suspend fun login(username: String, password: String): Result<User> {
        val params = mapOf("username" to username, "password" to password)
        return AppHttpClient.post("auth/login", params)
    }

    suspend fun getUserById(id: Int): Result<User> {
        return AppHttpClient.get("users/$id")
    }

}