package com.fd.fakestore.ui.profile

import com.fd.fakestore.data.model.User

sealed class ProfileState {
    data object Loading : ProfileState()
    data class Success(val user: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
}