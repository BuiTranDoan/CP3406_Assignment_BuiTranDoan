package com.studyhelpers.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyhelpers.app.data.local.entity.UserEntity
import com.studyhelpers.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val currentUser: UserEntity? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(username: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repo.login(username, password)
            _uiState.value = if (result.isSuccess) {
                val user = result.getOrNull()
                _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    currentUser = user,
                    errorMessage = null
                ).also { onSuccess() }
            } else {
                _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = false,
                    errorMessage = result.exceptionOrNull()?.message
                )
            }
        }
    }

    fun register(username: String, password: String, displayName: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repo.register(username, password, displayName)
            _uiState.value = if (result.isSuccess) {
                val user = result.getOrNull()
                _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    currentUser = user,
                    errorMessage = null
                ).also { onSuccess() }
            } else {
                _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = false,
                    errorMessage = result.exceptionOrNull()?.message
                )
            }
        }
    }
    fun logout() {
        _uiState.value = AuthUiState()
    }
}