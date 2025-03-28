package com.fd.fakestore.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fd.fakestore.data.local.preference.AppPreference
import com.fd.fakestore.data.repository.user.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: IUserRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val state: StateFlow<ProfileState> = _state

    fun getUserDetail() {
        viewModelScope.launch {
            val result = userRepository.getUserData()
            if (result.isSuccess) {
                val user = result.getOrNull()
                if (user != null) {
                    _state.update { ProfileState.Success(user) }
                } else {
                    _state.update { ProfileState.Error("User not found") }
                }
            } else {
                _state.update { ProfileState.Error(result.exceptionOrNull()?.message ?: "Unknown Error") }
            }
        }
    }

    fun clearUserData() {
        viewModelScope.launch {
            userRepository.clearUserData()
        }
    }
}