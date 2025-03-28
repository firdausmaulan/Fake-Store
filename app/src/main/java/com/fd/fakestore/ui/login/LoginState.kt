package com.fd.fakestore.ui.login

import com.fd.fakestore.data.model.User

sealed class LoginState {
    data object Initial : LoginState()
    data object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}