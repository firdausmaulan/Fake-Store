package com.fd.fakestore.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fd.fakestore.data.repository.user.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: IUserRepository
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Initial)
    val state: StateFlow<LoginState> = _state

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _state.update { LoginState.Loading }
            userRepository.login(username, password).fold(
                onSuccess = { user ->
                    _state.update { LoginState.Success(user) }
                },
                onFailure = { error ->
                    _state.update { LoginState.Error(error.message ?: "Unknown Error") }
                }
            )
        }
    }

    fun updateState(newState: LoginState) {
        _state.value = newState
    }

    suspend fun isAuthenticated(): Boolean {
        return userRepository.isAuthenticated()
    }
}