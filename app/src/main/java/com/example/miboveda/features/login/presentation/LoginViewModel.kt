package com.example.miboveda.features.login.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miboveda.features.login.domain.model.SignInFailure
import com.example.miboveda.features.login.domain.model.SignInResult
import com.example.miboveda.features.login.domain.repository.AuthRepository
import com.example.miboveda.features.login.domain.repository.SessionRepository
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        // La sesión guardada es la única fuente de verdad del usuario.
        viewModelScope.launch {
            sessionRepository.session.collect { user ->
                _uiState.update { it.copy(user = user, isRestoringSession = false) }
            }
        }
    }

    /**
     * Es suspend a propósito: la pantalla la lanza con su propio scope, así el Context de la
     * Activity no queda retenido por el ViewModel si el usuario rota o cierra la pantalla.
     */
    suspend fun signInWithGoogle(activityContext: Context) {
        if (_uiState.value.isLoading) return // evita doble toque

        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            when (val result = authRepository.signInWithGoogle(activityContext)) {
                is SignInResult.Success -> saveSession(result)
                SignInResult.Cancelled -> Unit
                is SignInResult.Failure -> _uiState.update { it.copy(error = result.reason) }
            }
        } finally {
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    suspend fun signOut(context: Context) {
        try {
            authRepository.signOut(context)
        } finally {
            sessionRepository.clear() // la sesión local se borra siempre
        }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(error = null) }
    }

    private suspend fun saveSession(result: SignInResult.Success) {
        try {
            sessionRepository.save(result.user)
        } catch (error: IOException) {
            _uiState.update { it.copy(error = SignInFailure.UNKNOWN) }
        }
    }
}