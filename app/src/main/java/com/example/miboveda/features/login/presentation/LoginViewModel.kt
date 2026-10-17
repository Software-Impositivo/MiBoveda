package com.example.miboveda.features.login.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.miboveda.features.login.domain.model.SignInResult
import com.example.miboveda.features.login.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Es suspend a propósito: la pantalla la lanza con su propio scope, así el Context de la
     * Activity no queda retenido por el ViewModel si el usuario rota o cierra la pantalla.
     */
    suspend fun signInWithGoogle(activityContext: Context) {
        if (_uiState.value.isLoading) return // evita doble toque

        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            val result = authRepository.signInWithGoogle(activityContext)
            _uiState.update { it.withResult(result) }
        } finally {
            _uiState.update { it.copy(isLoading = false) } // también si la corrutina se cancela
        }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(error = null) }
    }
}

private fun LoginUiState.withResult(result: SignInResult): LoginUiState = when (result) {
    is SignInResult.Success -> copy(user = result.user)
    SignInResult.Cancelled -> this
    is SignInResult.Failure -> copy(error = result.reason)
}